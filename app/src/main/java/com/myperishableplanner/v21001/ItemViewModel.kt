package com.myperishableplanner.v21001

import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myperishableplanner.v21001.dto.Item
import com.myperishableplanner.v21001.service.IItemService
import com.myperishableplanner.v21001.service.ItemService
import kotlinx.coroutines.launch
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.FirebaseFirestore
import com.myperishableplanner.v21001.dto.ItemDetail
import com.myperishableplanner.v21001.dto.Photo
import com.myperishableplanner.v21001.dto.User
import com.google.firebase.storage.FirebaseStorage
import kotlin.math.log


class ItemViewModel (var itemService: IItemService = ItemService()): ViewModel() {

        internal val NEW_ITEM = "New Item"
        var items : MutableLiveData<List<Item>> = MutableLiveData<List<Item>>()
        var itemDetails : MutableLiveData<List<ItemDetail>> = MutableLiveData<List<ItemDetail>>()
        var selectedItemDetail  by mutableStateOf(ItemDetail())
        var user: User? = null
        val photos: ArrayList<Photo> by mutableStateOf(ArrayList<Photo>())
        val eventPhotos : MutableLiveData<List<Photo>> = MutableLiveData<List<Photo>>()

        private lateinit var firestore : FirebaseFirestore
        private var storageReference = FirebaseStorage.getInstance().getReference()

        init
            {
                firestore = FirebaseFirestore.getInstance()
                firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
            }



    fun listenToItemDetails() {
        user?.let {
            user ->
            firestore.collection("users").document(user.uid).collection("itemDetails")
                .addSnapshotListener { snapshot, e ->
                    //handle the error if there is one , and then return
                    if (e != null) {
                        Log.w("Listen failed", e)
                        return@addSnapshotListener
                    }
                    // if we reached this point , there was not an error
                    snapshot?.let {
                        val allItemDetails = ArrayList<ItemDetail>()
                        allItemDetails.add(ItemDetail(itemName = NEW_ITEM))
                        val document = snapshot.documents
                        document.forEach {
                            val itemDetail = it.toObject(ItemDetail::class.java)
                            itemDetail?.let {
                                allItemDetails.add(it)
                            }
                        }
                        itemDetails.value = allItemDetails
                    }

                }
        }
    }


    fun fetchItems(){
            viewModelScope.launch {
                items.postValue(itemService.fetchItems())
            }
        }

    fun saveItemDetail() {

        user?.let { user ->
            val document =
                if (selectedItemDetail.itemDetailId == null || selectedItemDetail.itemDetailId.isEmpty()) {
                    firestore.collection("users").document(user.uid).collection("itemDetails").document()
                } else {
                    firestore.collection("users").document(user.uid).collection("itemDetails").document(selectedItemDetail.itemDetailId)
                }
            selectedItemDetail.itemDetailId = document.id
            document.set(selectedItemDetail)
            val handle = document.set(selectedItemDetail)
            handle.addOnSuccessListener { Log.d("Firebase", "Document Saved")
                if (photos.isNotEmpty()) {
                    uploadPhotos()
                }
            }
            handle.addOnFailureListener { Log.e("Firebase", "Document Saved") }
        }
    }

    fun saveUser() {
        user?.let {
            user ->
            val handle=  firestore.collection("users").document(user.uid).set(user)
            handle.addOnSuccessListener { Log.d("Firebase","Document Saved")}
            handle.addOnFailureListener { Log.e("Firebase","Document Saved")}
        }
        }

    private fun uploadPhotos() {
        photos.forEach {
                photo ->
            var uri = Uri.parse(photo.localUri)
            val imageRef = storageReference.child("images/${user?.uid}/${uri.lastPathSegment}")
            val uploadTask  = imageRef.putFile(uri)
            uploadTask.addOnSuccessListener {
                Log.i(TAG, "Image Uploaded $imageRef")
                val downloadUrl = imageRef.downloadUrl
                downloadUrl.addOnSuccessListener {
                        remoteUri ->
                    photo.remoteUri = remoteUri.toString()
                    updatePhotoDatabase(photo)

                }
            }
            uploadTask.addOnFailureListener {
                Log.e(TAG, it.message ?: "No message")
            }
        }
    }

    internal fun updatePhotoDatabase(photo: Photo) {
        user?.let { user ->
            var photoDocument = if (photo.id.isEmpty()) {
                firestore.collection("users").document(user.uid).collection("itemDetails")
                    .document(selectedItemDetail.itemDetailId).collection("photos").document()
            } else {
                firestore.collection("users").document(user.uid).collection("itemDetails")
                    .document(selectedItemDetail.itemDetailId).collection("photos")
                    .document(photo.id)
            }
            photo.id = photoDocument.id
            var handle = photoDocument.set(photo)
            handle.addOnSuccessListener {
                Log.i(TAG, "Successfully updated photo metadata")
                firestore.collection("users").document(user.uid).collection("itemDetails")
                    .document(selectedItemDetail.itemDetailId).collection("photos")
                    .document(photo.id).set(photo)
            }
            handle.addOnFailureListener {
                Log.e(TAG, "Error updating photo data: ${it.message}")
            }
        }
    }

        fun fetchPhotos() {
            photos.clear()
            user?.let {
                    user ->
                var photoCollection = firestore.collection("users").document(user.uid).collection("itemDetails").document(selectedItemDetail.itemDetailId).collection("photos")
                var photosListener = photoCollection.addSnapshotListener {
                        querySnapshot, firebaseFirestoreException ->
                    querySnapshot?.let {
                            querySnapshot ->
                        var documents = querySnapshot.documents
                        var inPhotos = ArrayList<Photo>()
                        documents?.forEach {
                            var photo = it.toObject(Photo::class.java)
                            photo?.let {
                            inPhotos.add(photo)                      }
                    }
                    eventPhotos.value = inPhotos
                }
            }
        }
     }

    fun delete(photo: Photo) {
        user?.let {
                user ->
            var photoCollection = firestore.collection("users").document(user.uid).collection("itemDetails").document(selectedItemDetail.itemDetailId).collection("photos")
            photoCollection.document(photo.id).delete()
            val uri = Uri.parse(photo.localUri)
            val imageRef = storageReference.child("images/${user.uid}/${uri.lastPathSegment}")
            imageRef.delete()
                .addOnSuccessListener {
                    Log.i(TAG, "Photo binary file deleted ${photo}")
                }
                .addOnFailureListener {
                    Log.e(TAG, "Photo delete failed.  ${it.message}")
                }
        }
    }


}


package com.myperishableplanner.v21001

import android.util.Log
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
import kotlin.math.log


class ItemViewModel (var itemService: IItemService = ItemService()): ViewModel() {


        var items : MutableLiveData<List<Item>> = MutableLiveData<List<Item>>()
        var itemDetails : MutableLiveData<List<ItemDetail>> = MutableLiveData<List<ItemDetail>>()

        private lateinit var firestore : FirebaseFirestore

        init
            {
                firestore = FirebaseFirestore.getInstance()
                firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
                listenToItemDetails()
            }

    private fun listenToItemDetails() {
        firestore.collection("itemDetails").addSnapshotListener{
            snapshot, e ->
            //handle the error if there is one , and then return
            if (e !=null){
                Log.w("Listen failed",e)
                return@addSnapshotListener
            }
          // if we reached this point , there was not an error
            snapshot?.let{
                val allItemDetails = ArrayList <ItemDetail>()
                val document  = snapshot.documents
                document.forEach{
                    val itemDetail = it.toObject(ItemDetail:: class.java)
                    itemDetail?.let{
                        allItemDetails.add(it)
                    }
                }
                itemDetails.value = allItemDetails
            }

        }
    }


    fun fetchItems(){
            viewModelScope.launch {
                items.postValue(itemService.fetchItems())
            }
        }

    fun saveItemDetail(itemDetail: ItemDetail) {
        val document = if (itemDetail.itemDetailId == null || itemDetail.itemDetailId.isEmpty()) {
            firestore.collection("itemDetails").document()
        } else {
            firestore.collection("itemDetails").document(itemDetail.itemDetailId)
        }
        itemDetail.itemDetailId = document.id
        document.set (itemDetail)
        val handle = document.set (itemDetail)
        handle.addOnSuccessListener { Log.d("Firebase","Document Saved")}
        handle.addOnFailureListener { Log.e("Firebase","Document Saved")}
    }
}
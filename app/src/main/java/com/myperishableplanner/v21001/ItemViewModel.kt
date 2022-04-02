package com.myperishableplanner.v21001

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
import kotlin.math.log


class ItemViewModel (var itemService: IItemService = ItemService()): ViewModel() {

        internal val NEW_ITEM = "New Item"
        var items : MutableLiveData<List<Item>> = MutableLiveData<List<Item>>()
        var itemDetails : MutableLiveData<List<ItemDetail>> = MutableLiveData<List<ItemDetail>>()
        var selectedItemDetail  by mutableStateOf(ItemDetail())

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
                allItemDetails.add(ItemDetail(itemName = NEW_ITEM))
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

    fun saveItemDetail() {
        val document = if (selectedItemDetail.itemDetailId == null || selectedItemDetail.itemDetailId.isEmpty()) {
            firestore.collection("itemDetails").document()
        } else {
            firestore.collection("itemDetails").document(selectedItemDetail.itemDetailId)
        }
        selectedItemDetail.itemDetailId = document.id
        document.set (selectedItemDetail)
        val handle = document.set (selectedItemDetail)
        handle.addOnSuccessListener { Log.d("Firebase","Document Saved")}
        handle.addOnFailureListener { Log.e("Firebase","Document Saved")}
    }
}
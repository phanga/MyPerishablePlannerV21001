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


class ItemViewModel (var itemService: IItemService = ItemService()): ViewModel() {


        var items : MutableLiveData<List<Item>> = MutableLiveData<List<Item>>()

        private lateinit var firestore : FirebaseFirestore

        init {
            {
                firestore = FirebaseFirestore.getInstance()
                firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
            }
        }

        fun fetchItems(){
            viewModelScope.launch {
                items.postValue(itemService.fetchItems())
            }
        }

    fun saveItemDetail(itemDetail: ItemDetail) {
        val document = firestore.collection("itemDetail").document()
        itemDetail.itemDetailId = document.id
        document.set (itemDetail)
        val handle = document.set (itemDetail)
        handle.addOnSuccessListener { Log.d("Firebase","Document Saved")}
        handle.addOnFailureListener { Log.e("Firebase","Document Saved")}
    }
}
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

    private var firestore : FirebaseFirestore = FirebaseFirestore.getInstance()
    var items : MutableLiveData<List<Item>> = MutableLiveData<List<Item>>()

    init {
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
        }

    fun fetchItems(){
        viewModelScope.launch {
            items.postValue(itemService.fetchItems())
        }
    }

    fun saveItemDetail(itemDetail: ItemDetail) {
        val document = if (itemDetail.itemDetailId == null || itemDetail.itemDetailId.isEmpty()) {
            firestore.collection("itemDetail").document()
        } else {
            firestore.collection("itemDetail").document(itemDetail.itemDetailId)
        }
        itemDetail.itemDetailId = document.id
        document.set (itemDetail)

        val handle = document.set (itemDetail)
        handle.addOnSuccessListener { Log.d("Firebase","Document Saved")}
        handle.addOnFailureListener { Log.e("Firebase","Document Saved")}
    }
}
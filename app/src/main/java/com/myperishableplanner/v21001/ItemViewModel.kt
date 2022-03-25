package com.myperishableplanner.v21001

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myperishableplanner.v21001.dto.Item
import com.myperishableplanner.v21001.service.IItemService
import com.myperishableplanner.v21001.service.ItemService
import kotlinx.coroutines.launch
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage


class ItemViewModel (var itemService: IItemService = ItemService()): ViewModel() {


        var items : MutableLiveData<List<Item>> = MutableLiveData<List<Item>>()

        private lateinit var firestore : FirebaseFirestore

        fun fetchItems(){
            viewModelScope.launch {
                items.postValue(itemService.fetchItems())
            }
        }
    }
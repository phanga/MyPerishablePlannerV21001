package com.myperishableplanner.v21001

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myperishableplanner.v21001.dto.Item
import com.myperishableplanner.v21001.service.ItemService
import kotlinx.coroutines.launch

class ItemViewModel (var itemService: ItemService= ItemService()): ViewModel() {
        var items : MutableLiveData<List<Item>> = MutableLiveData<List<Item>>()
        fun fetchItems(){
            viewModelScope.launch {
                items.postValue(itemService.fetchItems())
            }
        }
    }
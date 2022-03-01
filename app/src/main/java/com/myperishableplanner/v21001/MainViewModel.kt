package com.myperishableplanner.v21001

import androidx.lifecycle.ViewModel
import com.myperishableplanner.v21001.service.IItemService
import com.myperishableplanner.v21001.service.ItemService

class MainViewModel(var itemService: IItemService = ItemService()) : ViewModel() {
}
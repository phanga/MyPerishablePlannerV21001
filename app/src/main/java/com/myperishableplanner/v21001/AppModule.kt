package com.myperishableplanner.v21001


import com.myperishableplanner.v21001.service.IItemService
import com.myperishableplanner.v21001.service.ItemService
import org.koin.dsl.module
import org.koin.androidx.viewmodel.dsl.viewModel

@JvmField
val appModule = module {
    viewModel { ItemDetailViewModel(get()) }
    single <IItemService>{ ItemService() }
}
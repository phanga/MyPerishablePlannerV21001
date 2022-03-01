package com.myperishableplanner.v21001

import com.myperishableplanner.v21001.service.IItemService
import com.myperishableplanner.v21001.service.ItemService
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { MainViewModel(get()) }
    single<IItemService> { ItemService() }
}
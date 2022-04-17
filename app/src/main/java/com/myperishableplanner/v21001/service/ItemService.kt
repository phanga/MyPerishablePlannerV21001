package com.myperishableplanner.v21001.service


import com.myperishableplanner.v21001.RetrofitClientInstance
import com.myperishableplanner.v21001.dao.IItemDAO
import com.myperishableplanner.v21001.dto.Item
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse

interface IItemService {
    suspend fun fetchAllItems() : List<Item>?
    suspend fun fetchItems(searchTxt: String) : List<Item>?
    suspend fun fetchItembyId(itemId: Int)  : Item?
    suspend fun fetchSavedItems() : List<Item>?
}

class ItemService : IItemService {
    override suspend fun fetchAllItems() : List <Item>? {
        return withContext(Dispatchers.IO) {
            val service = RetrofitClientInstance.retrofitInstance?.create(IItemDAO::class.java)
            val items = async { service?.getAllItems()}
            var result = items.await()?.awaitResponse()?.body()
            return@withContext result
        }
    }

    override suspend fun fetchItems(searchTxt: String) : List <Item>? {
        return withContext(Dispatchers.IO) {
            val service = RetrofitClientInstance.retrofitInstance?.create(IItemDAO::class.java)
            val items = async { service?.getItems(searchTxt)}
            var result = items.await()?.awaitResponse()?.body()
            return@withContext result
        }
    }

    override suspend fun fetchItembyId(itemId: Int) : Item? {
        return withContext(Dispatchers.IO) {
            val service = RetrofitClientInstance.retrofitInstance?.create(IItemDAO::class.java)
            val items = async { service?.getItembyId(itemId)}
            var result = items.await()?.awaitResponse()?.body()
            return@withContext result
        }
    }

    override suspend fun fetchSavedItems() : List <Item>? {
        val items = ArrayList<Item>()
        return items
    }

}
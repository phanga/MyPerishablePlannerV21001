package com.myperishableplanner.v21001.service


import com.myperishableplanner.v21001.RetrofitClientInstance
import com.myperishableplanner.v21001.dao.IAllItemDAO
import com.myperishableplanner.v21001.dto.Item
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse

interface IItemService {
    suspend fun fetchItems() : List<Item>?
}

class ItemService : IItemService {
    override suspend fun fetchItems() : List <Item>? {
        return withContext(Dispatchers.IO) {
            val service = RetrofitClientInstance.retrofitInstance?.create(IAllItemDAO::class.java)
            val items = async { service?.getAllItems()}
            var result = items.await()?.awaitResponse()?.body()
            return@withContext result
        }
    }
}
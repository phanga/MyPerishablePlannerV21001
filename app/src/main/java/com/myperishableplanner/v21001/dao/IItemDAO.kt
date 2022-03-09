package com.myperishableplanner.v21001.dao

import com.myperishableplanner.v21001.dto.Item
import retrofit2.http.GET
import retrofit2.Call

interface IItemDAO {
    @GET("/Items")
    fun getAllItems() : Call<List<Item>>
}
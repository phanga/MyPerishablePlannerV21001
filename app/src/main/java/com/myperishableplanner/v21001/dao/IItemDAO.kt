package com.myperishableplanner.v21001.dao

import com.myperishableplanner.v21001.dto.Item
import retrofit2.Call
import retrofit2.http.GET

interface IItemDAO {
    @GET("fdc/v1/foods/list?api_key=but9Hwci1MFrkr6A3RXH0o7kgO9gjbHfgl01U2JC")
    fun getAllItems() : Call<List<Item>>
}
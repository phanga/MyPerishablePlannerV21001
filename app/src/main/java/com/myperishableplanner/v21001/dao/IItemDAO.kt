package com.myperishableplanner.v21001.dao

import com.myperishableplanner.v21001.dto.Item
import retrofit2.http.GET
import retrofit2.Call
import retrofit2.http.Query

interface IItemDAO {
    @GET("fdc/v1/foods/list")
    fun getAllItems(@Query("query") searchTxt: String, @Query("api_key") type: String? = "but9Hwci1MFrkr6A3RXH0o7kgO9gjbHfgl01U2JC"): Call<List<Item>>
}
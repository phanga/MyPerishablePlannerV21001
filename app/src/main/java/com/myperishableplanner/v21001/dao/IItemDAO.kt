package com.myperishableplanner.v21001.dao

import com.myperishableplanner.v21001.dto.Item
import retrofit2.http.GET
import retrofit2.Call

interface IItemDAO {
    @GET("fdc/v1/foods/list?api_key=but9Hwci1MFrkr6A3RXH0o7kgO9gjbHfgl01U2JC&pageSize=2&query=apple")
    fun getAllItems() : Call<List<Item>>
}
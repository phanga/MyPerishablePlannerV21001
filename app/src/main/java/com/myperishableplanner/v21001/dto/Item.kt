package com.myperishableplanner.v21001.dto

import com.google.gson.annotations.SerializedName

data class Item(@SerializedName("fdcId")var id: Int, @SerializedName("description") var name:String,@SerializedName("brandOwner") var brand:String){
    override fun toString(): String {
        return name + " " + brand
    }
}

package com.myperishableplanner.v21001.dto

data class Item(var productName: String, var life : String, var description :String, var itemId:Int = 0) {
    override fun toString(): String {
        return description
    }
    }
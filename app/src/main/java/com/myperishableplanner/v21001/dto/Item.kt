package com.myperishableplanner.v21001.dto

import java.util.*

data class Item(var itemName: String, var category: String, var expirationDate: Date, var description: String, var id: Int =0){
    override fun toString(): String {
        return "$itemName $category $expirationDate $description"
    }
}

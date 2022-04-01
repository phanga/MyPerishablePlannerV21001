package com.myperishableplanner.v21001.dto

data class Category ( var id : Int, var category : String , var storageInstruction : String ){
    override fun toString(): String {
        return category
    }
}
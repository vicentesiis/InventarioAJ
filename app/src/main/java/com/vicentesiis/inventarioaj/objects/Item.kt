package com.vicentesiis.inventarioaj.objects

data class Item(
    var category: String,
    var name: String,
    var count: Int,
    var price: Int
)
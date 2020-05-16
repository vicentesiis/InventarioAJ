package com.vicentesiis.inventarioaj.data

import io.realm.RealmObject
import java.util.*

/*
    This class persist the items
*/

open class Item (
    var name: String = "",
    var category: Category? = null,
    var price: Int = 0,
    var quantity: Int = 0,
    var id: String = UUID.randomUUID().toString()
): RealmObject()
package com.vicentesiis.inventarioaj.data

import io.realm.RealmObject
import java.util.*

/*
    This class persist the Sales
*/

open class Sale (
    var createdAt: Date = Date(),
    var item: Item? = null,
    var id: String = UUID.randomUUID().toString()
): RealmObject()
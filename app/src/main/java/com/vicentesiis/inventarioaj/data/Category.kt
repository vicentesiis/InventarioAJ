package com.vicentesiis.inventarioaj.data

import io.realm.RealmList
import io.realm.RealmObject
import java.util.*

/*
    This class persist the categories
*/

open class Category (
    var name: String = "",
    var color: String = "",
    var items: RealmList<Item> = RealmList(),
    var id: String = UUID.randomUUID().toString()
): RealmObject()
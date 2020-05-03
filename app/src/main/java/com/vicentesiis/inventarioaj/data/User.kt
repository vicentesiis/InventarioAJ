package com.vicentesiis.inventarioaj.data

import io.realm.RealmObject
import java.util.*

open class User (
    var name: String = "",
    var email: String = "",
    var password: String ="",
    var id: String = UUID.randomUUID().toString()
): RealmObject()
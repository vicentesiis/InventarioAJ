package com.vicentesiis.inventarioaj.data

import io.realm.RealmObject
import java.util.*

/*
    This class persist the session log
*/

open class UserLog (
    var createdAt: Date = Date(),
    var user: User? = null
): RealmObject()
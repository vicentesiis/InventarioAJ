package com.vicentesiis.inventarioaj.data

import android.content.ClipData
import com.vicentesiis.inventarioaj.data.Item
import io.realm.RealmList
import io.realm.RealmObject
import java.util.*

open class TemporalSale (
    var items: RealmList<Item>? = null,
    var total: Int = 0,
    var id: String = UUID.randomUUID().toString()
): RealmObject()
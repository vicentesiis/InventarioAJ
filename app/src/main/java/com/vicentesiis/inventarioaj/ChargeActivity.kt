package com.vicentesiis.inventarioaj

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.vicentesiis.inventarioaj.data.Sale
import com.vicentesiis.inventarioaj.data.TemporalSale
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where

class ChargeActivity : AppCompatActivity() {

    private lateinit var total: TextView

    private var temporalSale: TemporalSale? = null
    private var realm = Realm.getDefaultInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_charge)

        total = findViewById(R.id.total)

        temporalSale = realm.where<TemporalSale>().findFirst()

        if (temporalSale != null) {
            total.text = "Total a cobrar: $" + temporalSale?.total.toString()
        }

    }

    fun cancelDidPressed(view: View) {
        realm.executeTransaction {
            temporalSale?.items?.map {
                it.quantity += 1
            }
            temporalSale?.deleteFromRealm()
        }
        finish()
    }

    fun chargeDidPressed(view: View) {
        realm.executeTransaction {
            temporalSale?.items?.map {
                val sale = realm.createObject<Sale>()
                sale.item = it
            }
            temporalSale?.deleteFromRealm()
        }
        finish()
    }

}

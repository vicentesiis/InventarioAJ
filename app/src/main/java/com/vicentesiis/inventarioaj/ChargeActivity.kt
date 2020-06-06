package com.vicentesiis.inventarioaj

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.vicentesiis.inventarioaj.data.Sale
import com.vicentesiis.inventarioaj.data.TemporalSale
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.TemporalQueries.localDate
import java.util.*


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
        realm.executeTransaction {
            val sales = realm.where<Sale>().findAll()
            var date = LocalDate.of(2018, 12, 31)
            val datepro = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant())
            sales.first()?.createdAt = datepro
            var date2 = LocalDate.of(2020, 12, 31)
            val datepro2 = Date.from(date2.atStartOfDay(ZoneId.systemDefault()).toInstant())
            sales.last()?.createdAt = datepro2
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

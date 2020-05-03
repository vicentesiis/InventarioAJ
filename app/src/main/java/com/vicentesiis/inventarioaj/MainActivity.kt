package com.vicentesiis.inventarioaj

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.vicentesiis.inventarioaj.utils.Utils
import io.realm.Realm

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Realm.init(this)

        val sharedPreferences = getSharedPreferences(Utils.PREF_NAME, Utils.PRIVATE_MODE)

        val activityIntent = if (sharedPreferences.getBoolean("login", false)) {
            Intent(this, HomeActivity::class.java)
        } else {
            Intent(this, LoginActvity::class.java)
        }

        startActivity(activityIntent)

        finish()
    }

    fun startInventory(view: View) {}
}

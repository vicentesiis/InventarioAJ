package com.vicentesiis.inventarioaj

import android.content.SharedPreferences
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.vicentesiis.inventarioaj.data.User
import com.vicentesiis.inventarioaj.ui.SalesFragment
import com.vicentesiis.inventarioaj.utils.Utils
import io.realm.Realm
import io.realm.kotlin.where

class HomeActivity : AppCompatActivity(), SalesFragment.FragmentListener {

    private lateinit var appBarConfiguration: AppBarConfiguration

    val realm = Realm.getDefaultInstance()

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)
        if (fragment is SalesFragment) {
            fragment.setListener(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val sharedPreferences = getSharedPreferences(Utils.PREF_NAME, Utils.PRIVATE_MODE)
        val userID = sharedPreferences.getString("id","")

        val currentUser = realm.where<User>().equalTo("id", userID).findFirst()

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        val navView: NavigationView = findViewById(R.id.nav_view)

        val hView = navView.getHeaderView(0)

        val navName: TextView = hView.findViewById(R.id.name_header)
        navName.text = currentUser?.name

        val navEmail: TextView = hView.findViewById(R.id.email_header)
        navEmail.text = currentUser?.email

        val navController = findNavController(R.id.nav_host_fragment)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        appBarConfiguration = AppBarConfiguration(setOf(
                R.id.nav_home, R.id.nav_articles, R.id.nav_reports, R.id.nav_logout), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        var editor = sharedPreferences.edit()
        editor.putBoolean("login", true)
        editor.apply()

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onItemClick() {
        TODO("Not yet implemented")
    }

}

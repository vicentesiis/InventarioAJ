package com.vicentesiis.inventarioaj

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import org.w3c.dom.Text

class LoginActvity : AppCompatActivity() {

    private lateinit var email: EditText
    private lateinit var passwoed: EditText
    private lateinit var progessBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        email = findViewById(R.id.username)
        passwoed = findViewById(R.id.password)
        progessBar = findViewById(R.id.loading)

    }

    fun login(view: View) {

        login()

    }

    private fun login() {
        if (!(TextUtils.isEmpty(email.text.toString()) && TextUtils.isEmpty(passwoed.text.toString())) ) {
            progessBar.visibility = View.VISIBLE
        } else {
            Toast.makeText(this, "Introduzca las credenciales", Toast.LENGTH_LONG).show()
        }
    }


}

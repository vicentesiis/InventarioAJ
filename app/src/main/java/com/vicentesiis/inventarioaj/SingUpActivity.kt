package com.vicentesiis.inventarioaj

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.vicentesiis.inventarioaj.data.User
import com.vicentesiis.inventarioaj.data.UserLog
import com.vicentesiis.inventarioaj.utils.Utils
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_login.*
import org.w3c.dom.Text

class SingUpActivity : AppCompatActivity() {

    private lateinit var name: EditText
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var loading: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_singup)

        name = findViewById<EditText>(R.id.create_name)
        email = findViewById<EditText>(R.id.create_user)
        password = findViewById<EditText>(R.id.create_password)
        loading = findViewById<ProgressBar>(R.id.loading_pro)

    }

    fun singUp(view: View) {
        singUp()
    }

    private fun singUp() {
        if (!(TextUtils.isEmpty(email.text.toString()) &&
            TextUtils.isEmpty(email.text.toString()) &&
            TextUtils.isEmpty(password.text.toString())) &&
            isValidEmail(email.text.toString()) &&
            !emailExists(email.text.toString())) {

            loading.visibility = View.VISIBLE

            val realm = Realm.getDefaultInstance()

            realm.executeTransaction { realm ->

                val user = realm.createObject<User>()
                user.name = name.text.toString()
                user.email = email.text.toString()
                user.password = password.text.toString()

                val log = realm.createObject<UserLog>()
                log.user = user

            }

            val user = realm.where<User>().equalTo("email", email.text.toString()).findFirst()

            val sharedPreferences = getSharedPreferences(Utils.PREF_NAME, Utils.PRIVATE_MODE)
            val editor = sharedPreferences.edit()
            editor.putString("id", user?.id)
            editor.apply()

            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)

        } else {
            Toast.makeText(this, "Ups!, ha ocurrido un error", Toast.LENGTH_LONG).show()
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun emailExists(email: String): Boolean {
        val user = Realm.getDefaultInstance().where<User>().equalTo("email", email).findFirst()
        if (user != null) { return true }
        return false
    }

}
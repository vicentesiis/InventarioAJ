package com.vicentesiis.inventarioaj

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
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
import java.net.UnknownServiceException

class LoginActvity : AppCompatActivity() {

    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var progessBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        email = findViewById(R.id.username)
        password = findViewById(R.id.password)
        progessBar = findViewById(R.id.loading)

    }

    fun login(view: View) {
        if (!(TextUtils.isEmpty(email.text.toString()) &&
            TextUtils.isEmpty(password.text.toString())) &&
            isValidEmail(email.text.toString())) {

            if (emailExists(email.text.toString()) != null) {

                val user = emailExists(email.text.toString())

                if (user?.password != password.text.toString()) {
                    Toast.makeText(this, "Ups!, las credenciales no coinciden", Toast.LENGTH_LONG).show()
                    return
                }

                progessBar.visibility = View.VISIBLE

                val realm = Realm.getDefaultInstance()

                realm.executeTransaction { realm ->

                    val log = realm.createObject<UserLog>()
                    log.user = user

                }

                val sharedPreferences = getSharedPreferences(Utils.PREF_NAME, Utils.PRIVATE_MODE)
                val editor = sharedPreferences.edit()
                editor.putString("id", user?.id)
                editor.apply()

                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)

            } else {
                Toast.makeText(this, "Ups!, el usuario no existe", Toast.LENGTH_LONG).show()
            }



        } else {
            Toast.makeText(this, "Ups!, ha ocurrido un error", Toast.LENGTH_LONG).show()
        }
    }


    fun singup(view: View) {
        val intent = Intent(this, SingUpActivity::class.java)
        startActivity(intent)
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun emailExists(email: String): User? {
        val user = Realm.getDefaultInstance().where<User>().equalTo("email", email).findFirst()
        if (user != null) { return user }
        return null
    }

}

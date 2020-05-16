package com.vicentesiis.inventarioaj

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.vicentesiis.inventarioaj.data.Category
import com.vicentesiis.inventarioaj.data.User
import com.vicentesiis.inventarioaj.data.UserLog
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_login.*


class EditCategoryActivity : AppCompatActivity() {

    private lateinit var itemName: EditText
    private lateinit var buttonSave: Button
    private lateinit var buttonDelete: Button

    private var lastButtonChosee: Button? = null

    var edit = false
    var category: Category? = null

    private var realm = Realm.getDefaultInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_category)

        itemName = findViewById(R.id.item_name)
        buttonSave = findViewById(R.id.save)
        buttonDelete = findViewById(R.id.delete)

        val categoryID = intent.getStringExtra("categoryID")

        if (categoryID != null) {
            category = realm.where<Category>().contains("id", categoryID).findFirst()
            buttonSave.text = "Guardar cambios"
            itemName.setText(category!!.name)
            edit = true
        }
    }

    fun chooseColor(view: View) {
        val id = resources.getResourceName(view.id)
        val button = view as Button
        if (lastButtonChosee != button) {
            button.text = "V"
            button.textSize = 30f

            lastButtonChosee?.text = ""
            lastButtonChosee = button
        }
    }

    fun delete(view: View) {
        if (edit) {
            realm.executeTransaction {
                category?.deleteFromRealm()
            }
        }
        finish()
    }

    fun save(view: View) {
        if (!TextUtils.isEmpty(itemName.text.toString()) && lastButtonChosee != null) {

            val draw: ColorDrawable = lastButtonChosee!!.background as ColorDrawable
            val color_id = draw.getColor()
            val colorHex = Integer.toHexString(color_id)

            if (!edit) {

                realm.executeTransaction {

                    val category = realm.createObject<Category>()
                    category.name = itemName.text.toString()
                    category.color = colorHex
                    finish()
                }

            } else {

                realm.executeTransaction {
                    category?.name = itemName.text.toString()
                    category?.color = colorHex
                    finish()
                }

            }

        } else {
            Toast.makeText(this, "Ups!, Rellena todos los campos", Toast.LENGTH_LONG).show()
        }
    }

}

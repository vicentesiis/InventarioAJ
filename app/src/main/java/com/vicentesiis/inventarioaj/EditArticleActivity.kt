package com.vicentesiis.inventarioaj

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import com.vicentesiis.inventarioaj.R
import com.vicentesiis.inventarioaj.data.Category
import com.vicentesiis.inventarioaj.data.Item
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlin.math.cos

class EditArticleActivity : AppCompatActivity() {

    private lateinit var buttonDelete: Button
    private lateinit var buttonSave: Button

    private lateinit var itemName: EditText
    private lateinit var category: Spinner
    private lateinit var cost: EditText
    private lateinit var inventory: EditText

    private var realm = Realm.getDefaultInstance()
    private var edit = false
    private val categories = realm.where<Category>().findAll()
    private var article: Item? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_article)
        
        buttonSave = findViewById(R.id.save)
        buttonDelete = findViewById(R.id.delete)
        itemName = findViewById(R.id.item_name)
        category = findViewById(R.id.spinner)
        cost = findViewById(R.id.cost)
        inventory = findViewById(R.id.inventory)

        val adapter = ArrayAdapter(this,
            R.layout.support_simple_spinner_dropdown_item,
            categories.map { category -> category.name })
        category.adapter = adapter

        val articleID = intent.getStringExtra("articleID")

        if (articleID != null) {

            article = realm.where<Item>().contains("id", articleID).findFirst()
            buttonSave.text = "Guardar cambios"
            itemName.setText(article?.name)
            cost.setText(article?.price.toString())
            inventory.setText(article?.quantity.toString())
            val position = categories.indexOf(article?.category)
            category.setSelection(position)
            edit = true
        }

    }

    fun save(view: View) {

        if (!(TextUtils.isEmpty(itemName.text.toString()) &&
            TextUtils.isEmpty(category.selectedItem.toString()) &&
            TextUtils.isEmpty(cost.text.toString()) &&
            TextUtils.isEmpty(inventory.text.toString()))) {

            if (edit) {

                val categorySelected = categories[category.selectedItemPosition]

                realm.executeTransaction {
                    article?.name = itemName.text.toString()
                    article?.price = cost.text.toString().toInt()
                    article?.quantity = inventory.text.toString().toInt()
                    article?.category = categorySelected
                    finish()
                }

            } else {

                val categorySelected = categories[category.selectedItemPosition]

                realm.executeTransaction {
                    val article = realm.createObject<Item>()
                    article.name = itemName.text.toString()
                    article.price = cost.text.toString().toInt()
                    article.quantity = inventory.text.toString().toInt()
                    article.category = categorySelected
                    finish()
                }

            }


        } else {
            Toast.makeText(this, "Ups!, Rellena todos los campos", Toast.LENGTH_LONG).show()
        }

    }

    fun delete(view: View) {
        if (edit) {
            realm.executeTransaction {
                article?.deleteFromRealm()
            }
        }
        finish()
    }

}

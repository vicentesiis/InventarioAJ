package com.vicentesiis.inventarioaj

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.vicentesiis.inventarioaj.data.Category
import com.vicentesiis.inventarioaj.data.Item
import com.vicentesiis.inventarioaj.ui.SalesFragment
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.fragment_sales.*


class ArticleActivity : AppCompatActivity(), SalesFragment.FragmentListener {

    private var list: ListView? = null

    val realm = Realm.getDefaultInstance()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            val intent = Intent(this, EditArticleActivity::class.java)
            startActivity(intent)
        }

        supportActionBar?.hide()

    }

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)
        if (fragment is SalesFragment) {
            fragment.setListener(this)
            fragment.makeSales = false
        }
    }

    override fun onItemClick(item: Item) {
        val intent = Intent(this, EditArticleActivity::class.java)
        intent.putExtra("articleID", item.id)
        startActivity(intent)
    }

}

package com.vicentesiis.inventarioaj

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.vicentesiis.inventarioaj.ui.SalesFragment


class ArticleActivity : AppCompatActivity(), SalesFragment.FragmentListener {

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
        }
    }

    override fun onItemClick() {
        val intent = Intent(this, EditArticleActivity::class.java)
        startActivity(intent)
    }

}

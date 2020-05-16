package com.vicentesiis.inventarioaj.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.vicentesiis.inventarioaj.*


class ArticlesFragment : Fragment() {

    private lateinit var articlesViewModel: ArticlesViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        articlesViewModel = ViewModelProvider(this).get(ArticlesViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_articles, container, false)

        val list = root.findViewById<ListView>(R.id.articles_list)

        val arrayAdapter = ArrayAdapter(context!!, android.R.layout.simple_list_item_1, articlesViewModel.getItems())
        list.adapter = arrayAdapter

        list.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->

                if (position == 0) {
                    val intent = Intent(activity, ArticleActivity::class.java)
                    startActivity(intent)
                } else {
                    val intent = Intent(activity, CategoryActivity::class.java)
                    startActivity(intent)
                }

            }

        return root
    }



}

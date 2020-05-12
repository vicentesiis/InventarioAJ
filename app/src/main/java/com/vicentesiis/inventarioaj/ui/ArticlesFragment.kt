package com.vicentesiis.inventarioaj.ui

import android.R.attr.fragment
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.vicentesiis.inventarioaj.ContainerActivitySalesFragment
import com.vicentesiis.inventarioaj.HomeActivity
import com.vicentesiis.inventarioaj.R


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
                    val intent = Intent(activity, ContainerActivitySalesFragment::class.java)
                    startActivity(intent)
                } else {

                }

            }

        return root
    }



}

package com.vicentesiis.inventarioaj.ui

import android.content.Intent
import android.icu.util.ULocale
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import com.vicentesiis.inventarioaj.EditCategoryActivity

import com.vicentesiis.inventarioaj.R
import com.vicentesiis.inventarioaj.data.Category
import io.realm.Realm
import io.realm.kotlin.where

class CategoryFragment : Fragment() {

    private lateinit var viewModel: CategoryViewModel

    private var list: ListView? = null

    val realm = Realm.getDefaultInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.category_fragment, container, false)

        list = root.findViewById<ListView>(R.id.articles_list)

        val categories = realm.where<Category>().findAll()

        val adapter = ArrayAdapter(context!!, android.R.layout.simple_list_item_1, categories.map { category -> category.name })
        list?.adapter = adapter
        list?.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->

                val categorySelected = categories[position]

                val intent = Intent(activity, EditCategoryActivity::class.java)
                intent.putExtra("categoryID", categorySelected?.id)
                startActivity(intent)

            }

        return root
    }

    override fun onStart() {
        super.onStart()
        val categories = realm.where<Category>().findAll()
        var adapter = ArrayAdapter(context!!, android.R.layout.simple_list_item_1, categories.map { category -> category.name })
        list?.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CategoryViewModel::class.java)
        // TODO: Use the ViewModel
    }

}

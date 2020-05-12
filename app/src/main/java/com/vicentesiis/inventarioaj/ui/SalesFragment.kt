package com.vicentesiis.inventarioaj.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vicentesiis.inventarioaj.R
import com.vicentesiis.inventarioaj.ui.adapters.ItemAdapter

class SalesFragment : Fragment() {

    private lateinit var homeViewModel: SalesViewModel

    private lateinit var recyclerView: RecyclerView
    private lateinit var sales: TextView

    private val mAdapter: ItemAdapter = ItemAdapter()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        homeViewModel = ViewModelProvider(this).get(SalesViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_sales, container, false)

        sales = root.findViewById(R.id.sales)

        mAdapter.ItemAdapter(homeViewModel.getItems(), context!!)
        mAdapter.onItemClick = { item ->
            Toast.makeText(context, item.name, Toast.LENGTH_SHORT)
        }

        recyclerView = root.findViewById(R.id.items_recyclerView)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = mAdapter

        return root
    }



}
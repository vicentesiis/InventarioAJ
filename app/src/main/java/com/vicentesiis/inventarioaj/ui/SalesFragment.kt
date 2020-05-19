package com.vicentesiis.inventarioaj.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vicentesiis.inventarioaj.ChargeActivity
import com.vicentesiis.inventarioaj.R
import com.vicentesiis.inventarioaj.data.Item
import com.vicentesiis.inventarioaj.data.TemporalSale
import com.vicentesiis.inventarioaj.ui.adapters.ItemAdapter
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where


class SalesFragment : Fragment() {

    private lateinit var homeViewModel: SalesViewModel

    private lateinit var recyclerView: RecyclerView
    private lateinit var sales: Button

    private val mAdapter: ItemAdapter = ItemAdapter()
    private var mListener: FragmentListener? = null

    var makeSales = true
    private val realm = Realm.getDefaultInstance()
    private var temporalSale: TemporalSale? = null

    fun setListener(callback: FragmentListener) {
        mListener = callback
    }

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
            mListener?.onItemClick(item)

            if (makeSales) {

                temporalSale = realm.where<TemporalSale>().findFirst()
1
                if (item.quantity > 0) {
                    realm.executeTransaction {
                        if (temporalSale == null) { temporalSale = realm.createObject() }
                        item.quantity -= 1
                        temporalSale!!.items?.add(item)
                        temporalSale!!.total += item.price
                    }
                    sales.isEnabled = true
                    sales.text = "Cargo: $${temporalSale?.total}"
                    mAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(activity, "Ups!, no hay mas en el inventario", Toast.LENGTH_SHORT).show()
                }


            }

        }

        recyclerView = root.findViewById(R.id.items_recyclerView)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = mAdapter

        return root
    }

    override fun onStart() {
        super.onStart()
        mAdapter.ItemAdapter(homeViewModel.getItems(), context!!)
        recyclerView.adapter = mAdapter
        mAdapter.notifyDataSetChanged()

        temporalSale = realm.where<TemporalSale>().findFirst()

        if (temporalSale != null) {
            sales.isEnabled = true
            sales.text = "Cargo: $${temporalSale?.total}"
        } else {
            sales.isEnabled = false
            sales.text = "Cargo: $0"
        }
    }

    interface FragmentListener {
        fun onItemClick(item: Item)
    }

}

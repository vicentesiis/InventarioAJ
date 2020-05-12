package com.vicentesiis.inventarioaj.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.vicentesiis.inventarioaj.R

class ReportsFragment : Fragment() {

    private lateinit var reportsViewModel: ReportsViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        reportsViewModel = ViewModelProvider(this).get(ReportsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_reports, container, false)
        val listView = root.findViewById<ListView>(R.id.reports_list)
        val arrayAdapter = ArrayAdapter(context!!, android.R.layout.simple_list_item_1, reportsViewModel.getReports())
        listView.adapter = arrayAdapter
        return root
    }
}

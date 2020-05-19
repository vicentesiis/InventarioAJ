package com.vicentesiis.inventarioaj.ui

import androidx.lifecycle.ViewModel

class ReportsViewModel : ViewModel() {

    fun getReports(): MutableList<String> {

        val reports = ArrayList<String>()

        reports.add("Disponibilidad por categoria")
        reports.add("Disponibilidad por producto")
        reports.add("Venta por producto")
        reports.add("Venta por categoria")
        reports.add("Venta por dia")
        reports.add("Venta por semana")
        reports.add("Venta por mes")
        reports.add("Sesiones")

        return  reports

    }

}
package com.vicentesiis.inventarioaj.ui

import androidx.lifecycle.ViewModel


class ArticlesViewModel : ViewModel() {

    fun getItems(): MutableList<String> {

        var items: MutableList<String> = ArrayList()

        items.add("Articulos")
        items.add("Categorias")

        return items

    }

}
package com.vicentesiis.inventarioaj.ui.sales

import androidx.lifecycle.ViewModel
import com.vicentesiis.inventarioaj.objects.Item

class SalesViewModel : ViewModel() {

    fun getItems(): MutableList<Item> {

        var items: MutableList<Item> = ArrayList()

        items.add(Item("sombras ", "Sombra 99 tonos", 55, 230))
        items.add(Item("sombras ", "Sombra 88 tonos", 20, 210))
        items.add(Item("sombras ", "Sombra MED", 25, 140))
        items.add(Item("sombras ", "Sombra NOR", 13, 120))
        items.add(Item("labiales ", "Mate", 30, 20))
        items.add(Item("labiales ", "Gloss", 52, 15))
        items.add(Item("labiales ", "Barra", 30, 15))
        items.add(Item("labiales ", "Deliniador", 42, 10))
        items.add(Item("rostro ", "Primer A", 3, 20))
        items.add(Item("rostro ", "Primer B", 6, 70))
        items.add(Item("rostro ", "Colageno", 13, 10))
        items.add(Item("rostro ", "Esponjas", 26, 12))
        items.add(Item("accesorios de cabello ", "Prendedores ", 60, 10))
        items.add(Item("accesorios de cabello", "Cepillo", 4, 30))

        return items

    }

}
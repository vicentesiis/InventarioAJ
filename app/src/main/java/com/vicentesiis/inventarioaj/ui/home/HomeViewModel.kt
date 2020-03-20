package com.vicentesiis.inventarioaj.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vicentesiis.inventarioaj.objects.Item

class HomeViewModel : ViewModel() {

    fun getItems(): MutableList<Item> {

        var items: MutableList<Item> = ArrayList()

        items.add(Item("Chocolate", 4, 55))
        items.add(Item("Chocolate2", 3, 30))
        items.add(Item("Chocolate3", 5, 2))
        items.add(Item("Chocolate4", 65, 55))
        items.add(Item("Chocolate5", 1, 55))
        items.add(Item("Chocolate6", 0, 55))
        return items

    }

}
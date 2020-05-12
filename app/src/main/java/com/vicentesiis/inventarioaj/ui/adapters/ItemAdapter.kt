package com.vicentesiis.inventarioaj.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.vicentesiis.inventarioaj.R
import com.vicentesiis.inventarioaj.objects.Item
import kotlin.random.Random

class ItemAdapter(): RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    var onItemClick: ((Item) -> Unit)? = null
    var items: MutableList<Item> = ArrayList()
    lateinit var context: Context

    fun ItemAdapter(items: MutableList<Item>, context: Context) {
        this.items = items
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.home_items, parent, false)
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items.get(position)
        holder.bind(item, context)
    }

    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(items[adapterPosition])
            }
        }

        val productImage = view.findViewById<ImageView>(R.id.product_image)
        val category = view.findViewById<TextView>(R.id.category)
        val name = view.findViewById<TextView>(R.id.item)
        val count = view.findViewById<TextView>(R.id.count)
        val price = view.findViewById<TextView>(R.id.price)

        @SuppressLint("SetTextI18n")
        fun bind(item: Item, context: Context) {
            category.text = item.category
            name.text =  item.name
            count.text = "Cant: " + item.count.toString()
            price.text = "$" + item.price.toString()

        }

    }

}
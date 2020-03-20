package com.vicentesiis.inventarioaj.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.vicentesiis.inventarioaj.R
import com.vicentesiis.inventarioaj.objects.Item

class ItemAdapter(): RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

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

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val productImage = view.findViewById<ImageView>(R.id.product_image)
        val product = view.findViewById<TextView>(R.id.item)
        val count = view.findViewById<TextView>(R.id.count)
        val price = view.findViewById<TextView>(R.id.price)

        fun bind(item: Item, context: Context) {
            product.text = item.product
            count.text = item.count.toString() + " en almacen"
            price.text = item.price.toString() + "$"
            itemView.setOnClickListener(View.OnClickListener { Toast.makeText(context, product.text, Toast.LENGTH_SHORT) })

        }

    }

}
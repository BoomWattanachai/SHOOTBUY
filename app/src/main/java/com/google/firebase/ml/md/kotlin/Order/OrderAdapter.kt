package com.google.firebase.ml.md.kotlin.Order

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.ml.md.R
import com.squareup.picasso.Picasso
import java.text.NumberFormat

class OrderAdapter(val orderList: ArrayList<OrderData>) : RecyclerView.Adapter<OrderAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val order_product_image = itemView.findViewById(R.id.order_product_image) as ImageView
        val order_product_name = itemView.findViewById(R.id.order_product_name) as TextView
        val order_product_price = itemView.findViewById(R.id.order_product_price) as TextView
        val order_product_quantity = itemView.findViewById(R.id.order_product_quantity) as TextView
        val order_product_total_price = itemView.findViewById(R.id.order_product_total_price) as TextView


    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.order_item, parent, false)

        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return  orderList.size
    }

    override fun onBindViewHolder(holder: OrderAdapter.ViewHolder, position: Int) {

        val order = orderList[position]
        Picasso.get().load(order.imageResource).into(holder.order_product_image)
        holder.order_product_name.text = order.nameData
        holder.order_product_price.text = "฿"+ NumberFormat.getInstance().format(order.price).toString()
        holder.order_product_total_price.text = "Total ฿"+ NumberFormat.getInstance().format(order.totalPrice).toString()
        holder.order_product_quantity.text = order.amount.toString()

    }

}
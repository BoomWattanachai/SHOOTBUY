package com.google.firebase.ml.md.kotlin.HistoryOrder.OrderDetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.ml.md.R
import com.squareup.picasso.Picasso
import java.text.NumberFormat

class OrderDetailAdapter(val orderDetailList: ArrayList<OrderDetailData>) : RecyclerView.Adapter<OrderDetailAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val history_detail_order_product_image = itemView.findViewById(R.id.history_detail_order_product_image) as ImageView
        val history_detail_order_product_name = itemView.findViewById(R.id.history_detail_order_product_name) as TextView
        val history_detail_order_product_price = itemView.findViewById(R.id.history_detail_order_product_price) as TextView
        val history_detail_order_product_quantity = itemView.findViewById(R.id.history_detail_order_product_quantity) as TextView
        val history_detail_order_product_total_price = itemView.findViewById(R.id.history_detail_order_product_total_price) as TextView


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.detail_order_item, parent, false)

        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
       return orderDetailList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val orderDetail = orderDetailList[position]
        Picasso.get().load(orderDetail.imageResource).into(holder.history_detail_order_product_image)
        holder.history_detail_order_product_name.text = orderDetail.nameData
        holder.history_detail_order_product_price.text = "฿"+ NumberFormat.getInstance().format(orderDetail.price).toString()
        holder.history_detail_order_product_total_price.text = "Total ฿"+NumberFormat.getInstance().format((orderDetail.price!! * orderDetail.quantity!!)).toString()
        holder.history_detail_order_product_quantity.text = orderDetail.quantity.toString()
    }
}
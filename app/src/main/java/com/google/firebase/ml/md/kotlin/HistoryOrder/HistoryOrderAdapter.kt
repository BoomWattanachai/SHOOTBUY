package com.google.firebase.ml.md.kotlin.HistoryOrder

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.ml.md.R
import com.google.firebase.ml.md.ShootBuyMainActivity
import com.google.firebase.ml.md.kotlin.HistoryOrder.OrderDetail.HistoryOrderDetailFragment
import com.google.firebase.ml.md.kotlin.HistoryOrder.OrderDetail.OrderDetailActivity

class HistoryOrderAdapter(val historyOrderList: ArrayList<HistoryOrderData>,val historyOrderFragment:HistoryOrderFragment, val context: Context) : RecyclerView.Adapter<HistoryOrderAdapter.ViewHolder>(){

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val history_order_number = itemView.findViewById(R.id.history_order_number) as TextView
        val history_order_status = itemView.findViewById(R.id.history_order_status) as TextView
        val parentPanel = itemView.findViewById(R.id.parentPanel) as RelativeLayout

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryOrderAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.history_order_item, parent, false)

        return HistoryOrderAdapter.ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return historyOrderList.size
    }

    override fun onBindViewHolder(holder: HistoryOrderAdapter.ViewHolder, position: Int) {
        val historyOrder: HistoryOrderData = historyOrderList[position]

        holder.history_order_number.text = historyOrder.orderNumber.toString()

        if (historyOrder.status == 1)
        {
            holder.history_order_status.text = "Pending"
            holder.history_order_status.setTextColor(Color.RED)
        }else if (historyOrder.status == 2)
        {
            holder.history_order_status.text = "Shipping"
            holder.history_order_status.setTextColor(Color.YELLOW)
        }
        else if (historyOrder.status == 3)
        {
            holder.history_order_status.text = "Success"
            holder.history_order_status.setTextColor(Color.GREEN)
        }



        holder.parentPanel.setOnClickListener {
//            val intent = Intent(context, OrderDetailActivity::class.java)
//            intent.putExtra("orderNumber", historyOrder.orderNumber.toString())
//            startActivity(context, intent, null)
            var bundle = Bundle()
            bundle.putString("orderNumber", historyOrder.orderNumber.toString())

            var historyOrderDetailFragment = HistoryOrderDetailFragment().apply {
                arguments = Bundle().apply {
                    putString("orderNumber", historyOrder.orderNumber.toString())
                }
            }

            (context as FragmentActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.fl_main, historyOrderDetailFragment)
                    .commit()
        }
    }
}
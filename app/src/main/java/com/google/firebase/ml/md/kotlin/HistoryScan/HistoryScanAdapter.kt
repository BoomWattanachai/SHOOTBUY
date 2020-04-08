package com.google.firebase.ml.md.kotlin.HistoryScan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.relativeui.Address.AddressAdapter
import com.google.firebase.ml.md.R
import com.squareup.picasso.Picasso
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HistoryScanAdapter(val historyScanList: ArrayList<HistoryScanData>) : RecyclerView.Adapter<HistoryScanAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val history_scan_product_image = itemView.findViewById(R.id.history_scan_product_image) as ImageView
        val history_scan_product_name = itemView.findViewById(R.id.history_scan_product_name) as TextView
        val history_scan_product_model = itemView.findViewById(R.id.history_scan_product_model) as TextView
        val history_scan_product_price = itemView.findViewById(R.id.history_scan_product_price) as TextView



        val history_scan_product_time = itemView.findViewById(R.id.history_scan_product_time) as TextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryScanAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.history_scan_item, parent, false)

        return HistoryScanAdapter.ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return  historyScanList.size
    }

    override fun onBindViewHolder(holder: HistoryScanAdapter.ViewHolder, position: Int) {
        val historyScan: HistoryScanData = historyScanList[position]

        Picasso.get().load(historyScan.imageResource).into(holder.history_scan_product_image)
        holder.history_scan_product_name.text = historyScan.nameData
        holder.history_scan_product_model.text = historyScan.productModel
        holder.history_scan_product_price.text = "฿"+ NumberFormat.getInstance().format(historyScan.price).toString()

        var dateTime = historyScan.dateTime
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm")
        parser.timeZone = TimeZone.getTimeZone("THA")
        val formattedDate =
                formatter.format(parser.parse(dateTime)!!)

        holder.history_scan_product_time.text = formattedDate
    }
}
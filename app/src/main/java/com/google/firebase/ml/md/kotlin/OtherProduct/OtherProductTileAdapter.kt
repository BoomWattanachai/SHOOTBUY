package com.google.firebase.ml.md.kotlin.OtherProduct

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.ml.md.R
import com.google.firebase.ml.md.kotlin.EntityModels.ProductData.Tile
import com.google.firebase.ml.md.kotlin.LiveObjectDetectionActivity
import com.google.firebase.ml.md.kotlin.LiveObjectDetectionFragment
import com.squareup.picasso.Picasso

class OtherProductTileAdapter(val otherProductList: List<Tile>, val context: LiveObjectDetectionFragment) : RecyclerView.Adapter<OtherProductTileAdapter.ViewHolder>(){

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val other_product_image = itemView.findViewById(R.id.other_product_image) as ImageView
        val other_product_name = itemView.findViewById(R.id.other_product_name) as TextView
        val other_product_panel = itemView.findViewById(R.id.other_product_panel) as RelativeLayout
//        var addressId:Int? = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OtherProductTileAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.other_product_tile_item, parent, false)

        return OtherProductTileAdapter.ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return otherProductList.size
    }

    override fun onBindViewHolder(holder: OtherProductTileAdapter.ViewHolder, position: Int) {
        val otherProduct = otherProductList[position]
        Picasso.get().load(otherProduct.tileImage).into(holder.other_product_image)
        holder.other_product_name.text = otherProduct.tileModel


        holder.other_product_panel.setOnClickListener {
//            var changeData = LiveObjectDetectionActivity()
//            changeData.setData(otherProductList[position])
            context.setData(otherProductList[position])

        }

    }
}
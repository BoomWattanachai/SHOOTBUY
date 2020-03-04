package com.example.relativeui.Address

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.ml.md.R

var oldHolder: AddressAdapter.ViewHolder? = null

class AddressAdapter(val addressList: ArrayList<AddressData>) :
    RecyclerView.Adapter<AddressAdapter.ViewHolder>() {
//    var dataIsCheck: BooleanArray = BooleanArray(getItemCount())


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val address = itemView.findViewById(R.id.address) as TextView
        val radio = itemView.findViewById(R.id.radio) as RadioButton


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_select_address, parent, false)
//
//        for (i in 0..dataIsCheck.size - 1) {
//            dataIsCheck[i] = false
//        }

        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return addressList.size
    }

    override fun onBindViewHolder(holder: AddressAdapter.ViewHolder, position: Int) {

        //   holder.setIsRecyclable(false)

//        Log.d("test", "AAA")

        val address: AddressData = addressList[position]
        holder.address.text = address.address


//        if (dataIsCheck[position]) {
//            holder.radio.isChecked = true
//        }
//        else if (dataIsCheck[position] == false ) {
//            holder.radio.isChecked = false
//        }

        holder.radio.setOnClickListener {
//            dataIsCheck[position] = true
//            notifyDataSetChanged()
            addressList[holder.layoutPosition].check = true
            if (oldHolder != null && oldHolder != holder) {
//                Log.d("oldHolder",oldHolder!!.itemId.toString())
//                Log.d("oldHolder",oldHolder!!.adapterPosition.toString())
//                Log.d("oldHolder", oldHolder!!.layoutPosition.toString())
//                Log.d("oldHolder",oldHolder!!.toString())
//                Log.d("holder",holder.toString())
//                Log.d("holder", holder.layoutPosition.toString())
                addressList[(oldHolder!!.adapterPosition)].check = false
                oldHolder!!.radio.isChecked = false

            }
//
            oldHolder = holder

        }

        holder.radio.isChecked = addressList[holder.layoutPosition].check


        Log.d("rr","sad")

    }
}
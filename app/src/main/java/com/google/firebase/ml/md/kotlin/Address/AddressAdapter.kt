package com.example.relativeui.Address

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.ml.md.R
import com.google.firebase.ml.md.kotlin.Address.SelectAddressActivity
import com.google.firebase.ml.md.kotlin.Address.SelectAddressFragment
import com.google.firebase.ml.md.kotlin.HistoryOrder.OrderDetail.HistoryOrderDetailFragment
import com.google.firebase.ml.md.kotlin.Order.OderActivity
import com.google.firebase.ml.md.kotlin.Order.OrderFragment

var oldHolder: AddressAdapter.ViewHolder? = null

class AddressAdapter(val addressList: ArrayList<AddressData>, var selectAddressActivity: SelectAddressFragment, val context: Context) : RecyclerView.Adapter<AddressAdapter.ViewHolder>() {
//    var dataIsCheck: BooleanArray = BooleanArray(getItemCount())
private val TAG_FRAGMENT = "TAG_FRAGMENT"

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val address = itemView.findViewById(R.id.address) as TextView
        val radio = itemView.findViewById(R.id.radio) as RadioButton
        var addressId:Int? = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.select_address_item, parent, false)
        oldHolder = null
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
        holder.addressId = address.addressId


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
//            Log.d("Address",holder.address.text.toString())

        }


        holder.radio.isChecked = addressList[holder.layoutPosition].check



        selectAddressActivity.btnCheckout?.setOnClickListener {
//            val intent = Intent(context, OderActivity::class.java)
////            Log.d("Address",oldHolder!!.address.text.toString())
//            intent.putExtra("Address", oldHolder!!.address.text.toString())
//            intent.putExtra("AddressId", oldHolder!!.addressId.toString())
//            intent.putExtra("FullName", selectAddressActivity.fullName)
//            startActivity(context, intent, null)

            if(oldHolder != null)
            {
                var orderFragment = OrderFragment().apply {
                    arguments = Bundle().apply {
                        putString("Address", oldHolder!!.address.text.toString())
                        putString("AddressId",  oldHolder!!.addressId.toString())
                        putString("FullName", selectAddressActivity.fullName)
                    }
                }

                (context as FragmentActivity).supportFragmentManager.beginTransaction()
                        .replace(R.id.fl_main, orderFragment, TAG_FRAGMENT)
                        .addToBackStack(null)
                        .commit()
            }



        }


    }
}
package com.google.firebase.ml.md.kotlin.HistoryOrder

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.ml.md.R
import com.google.firebase.ml.md.kotlin.EntityModels.ProductOrder.Order
import com.google.firebase.ml.md.kotlin.HistoryOrder.OrderDetail.HistoryOrderDetailFragment
import com.google.firebase.ml.md.kotlin.IPAddress
import com.google.firebase.ml.md.kotlin.Models.Service.ProductOrder.GetAllProductOrderByUuid
import kotlinx.android.synthetic.main.activity_history_order.*


class HistoryOrderFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var v = inflater.inflate(R.layout.activity_history_order,container,false)
//        return super.onCreateView(inflater, container, savedInstanceState)
        val pref = this.activity!!.getSharedPreferences("SP_USER_DATA", Context.MODE_PRIVATE)
        val recyclerView: RecyclerView = v.findViewById(R.id.historyOrderRecycleView)

        var historyOrderList = ArrayList<HistoryOrderData>()


        var urlGetAllProductOrder = IPAddress.ipAddress + "product-order/getAllProductOrderByUuid/" + pref.getString("UUID", "")

        val listenerAllProductOrder = object : GetAllProductOrderByUuid.getDataComplete {
            override fun getDataComplete(orderList: List<Order>) {
                for(i in 0 until orderList.size)
                {
                    historyOrderList.add(
                            HistoryOrderData(
                                    orderList[i].orderId,
                                    orderList[i].orderStatus,
                                    orderList[i].orderDateTime
                            )
                    )

                }




                recyclerView.apply {
                    layoutManager = LinearLayoutManager(this@HistoryOrderFragment.context)
                    adapter = HistoryOrderAdapter(historyOrderList,this@HistoryOrderFragment,this@HistoryOrderFragment.context!!)
                }
            }
        }

        GetAllProductOrderByUuid(listenerAllProductOrder).execute(urlGetAllProductOrder)
//        historyOrderBackBtn.setOnClickListener {
//            onBackPressed()
//        }
        return v
    }



}
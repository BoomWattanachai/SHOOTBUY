package com.google.firebase.ml.md.kotlin.HistoryOrder

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.relativeui.Address.AddressAdapter
import com.example.relativeui.Address.AddressData
import com.google.firebase.ml.md.R
import com.google.firebase.ml.md.kotlin.EntityModels.ProductOrder.Order
import com.google.firebase.ml.md.kotlin.EntityModels.UserData.User
import com.google.firebase.ml.md.kotlin.IPAddress
import com.google.firebase.ml.md.kotlin.Models.Service.ProductOrder.GetAllProductOrderByUuid
import com.google.firebase.ml.md.kotlin.Models.Service.UserData.GetUserAddressByUuid
import kotlinx.android.synthetic.main.activity_history_order.*
import kotlinx.android.synthetic.main.activity_order_detail.*

class HistoryOrderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_order)


        val pref = getSharedPreferences("SP_USER_DATA", Context.MODE_PRIVATE)

        var historyOrderList = ArrayList<HistoryOrderData>()


        var urlGetAllProductOrder = IPAddress.ipAddress + "product-order/getAllProductOrderByUuid/" + pref.getString("UUID", "")

        val listenerAllProductOrder = object : GetAllProductOrderByUuid.getDataComplete {
            override fun getDataComplete(orderList: List<Order>) {
               for(i in 0 until orderList.size)
               {
                   historyOrderList.add(
                           HistoryOrderData(
                                   orderList[i].orderId,
                                   orderList[i].orderStatus
                           )
                   )

               }


                val recyclerView: RecyclerView = findViewById(R.id.historyOrderRecycleView)

                recyclerView.apply {
                    layoutManager = LinearLayoutManager(this@HistoryOrderActivity)
                    adapter = HistoryOrderAdapter(historyOrderList,this@HistoryOrderActivity,this@HistoryOrderActivity)
                }
            }
        }

        GetAllProductOrderByUuid(listenerAllProductOrder).execute(urlGetAllProductOrder)



















        historyOrderBackBtn.setOnClickListener {
            onBackPressed()
        }
    }
}

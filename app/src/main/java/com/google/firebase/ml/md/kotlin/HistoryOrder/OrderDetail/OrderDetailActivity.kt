package com.google.firebase.ml.md.kotlin.HistoryOrder.OrderDetail

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.ml.md.R
import com.google.firebase.ml.md.kotlin.EntityModels.ProductData.FoodAndBev
import com.google.firebase.ml.md.kotlin.EntityModels.ProductOrder.Order
import com.google.firebase.ml.md.kotlin.IPAddress
import com.google.firebase.ml.md.kotlin.Models.Service.ProductData.SelectProductFoodData
import com.google.firebase.ml.md.kotlin.Models.Service.ProductOrder.GetProductOrderByOrderId
import kotlinx.android.synthetic.main.activity_order_detail.*
import org.threeten.bp.Instant
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class OrderDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_detail)
        val orderNumber=intent.getStringExtra("orderNumber")

        order_detail_order_number.text = "Order #"+orderNumber



        var urlGetDetailProductOrder = IPAddress.ipAddress + "product-order/getProductOrderByOrderId/" + orderNumber
        GetProductOrderByOrderId(listenerSelectOrder).execute(urlGetDetailProductOrder)

        historyDetailOrderBackBtn.setOnClickListener {
            onBackPressed()
        }



    }



    val listenerSelectOrder = object : GetProductOrderByOrderId.getDataComplete {
        override fun getDataComplete(orderList: List<Order>) {
            val order = orderList[0]

            val orderDetail = order.orderDetail
            var orderDetailList = ArrayList<OrderDetailData>()
//            orderId = order.orderId

            for (i in 0 until orderDetail!!.size) {
                val categoryId = orderDetail[i].product!!.categoryId
                val productId = orderDetail[i].product!!.productId
                if (categoryId == 1) {
                    val urlSelectData = IPAddress.ipAddress + "product-data/selectProductFoodData/${productId}"

                    val listenerSelectData = object : SelectProductFoodData.getDataComplete {
                        override fun getDataComplete(foodAndBevList: List<FoodAndBev>) {

                            val foodAndBev = foodAndBevList[0]
                            orderDetailList.add(
                                    OrderDetailData(
                                            foodAndBev.foodAndBevImage,
                                            foodAndBev.foodAndBevBrand,
                                            orderDetail[i].quantity,
                                            foodAndBev.foodAndBevPrice,
                                            foodAndBev.foodAndBevPrice!!  *  orderDetail[i].quantity!!
                                    )
                            )
                            val recyclerView: RecyclerView = findViewById(R.id.historyDetailOrderRecycleView)

                            recyclerView.apply {
                                layoutManager = LinearLayoutManager(this@OrderDetailActivity)
                                adapter = OrderDetailAdapter(orderDetailList)
                            }


                        }

                    }
                    SelectProductFoodData(listenerSelectData).execute(urlSelectData)
                } else if (categoryId == 2) {
                } else if (categoryId == 3) {
                }
            }

            history_order_total_price.text = "฿" + order.totalPrice.toString()
            history_order_date.text = order.orderDateTime

        }

    }
}

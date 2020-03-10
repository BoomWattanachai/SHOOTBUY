package com.google.firebase.ml.md.kotlin.Order

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.ml.md.R
import com.google.firebase.ml.md.kotlin.EntityModels.ProductData.FoodAndBev
import com.google.firebase.ml.md.kotlin.EntityModels.ProductOrder.Order
import com.google.firebase.ml.md.kotlin.IPAddress
import com.google.firebase.ml.md.kotlin.LiveObjectDetectionActivity
import com.google.firebase.ml.md.kotlin.Models.Service.ProductData.SelectProductFoodData
import com.google.firebase.ml.md.kotlin.Models.Service.ProductOrder.CheckoutProductOrder
import com.google.firebase.ml.md.kotlin.Models.Service.ProductOrder.GetProductOrderByUuid
import kotlinx.android.synthetic.main.activity_oder.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList


class OderActivity : AppCompatActivity() {

    var orderId:Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_oder)


        val pref = getSharedPreferences("SP_USER_DATA", Context.MODE_PRIVATE)

        var urlGetType = IPAddress.ipAddress + "product-order/getProductOrderByUuid/" + pref.getString("UUID", "")
        GetProductOrderByUuid(listenerSelectOrder).execute(urlGetType)



        val address = intent.getStringExtra("Address")
        val addressId = intent.getStringExtra("AddressId")
        val fullName = intent.getStringExtra("FullName")

//        val localDate = LocalDate.now()
//        val formatter = DateTimeFormatter.ofPattern("dd MM yyyy")
//        val dateCurrent = formatter.format(localDate)

//        Log.d("localDate",localDate.toString())
//        Log.d("formatter",formatter.toString())
//        Log.d("dateCurrent",dateCurrent.toString())

//        var currentDate = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.ENGLISH)
        var SDFormat = SimpleDateFormat("dd/MM/yyyy")
        var currentDate = Date()
        var stringDate = SDFormat.format(currentDate)

        Address.text = address
        Name.text = fullName
        date.text = stringDate







        btnConfirm.setOnClickListener {
            var insertOrder = Order()
            insertOrder.orderId = orderId
            insertOrder.addressId = addressId.toInt()

            var urlCheckout = IPAddress.ipAddress + "product-order/checkoutProductOrder/"
            CheckoutProductOrder(insertOrder).execute(urlCheckout)
            startActivity(Intent(this, LiveObjectDetectionActivity::class.java))
        }
    }

    val listenerSelectOrder = object : GetProductOrderByUuid.getDataComplete {
        override fun getDataComplete(orderList: List<Order>) {
            val order = orderList[0]

            val orderDetail = order.orderDetail
            var orderListItem = ArrayList<OrderData>()
            orderId = order.orderId

            for (i in 0 until orderDetail!!.size) {
                val categoryId = orderDetail[i].product!!.categoryId
                val productId = orderDetail[i].product!!.productId
                if (categoryId == 1) {
                    val urlSelectData = IPAddress.ipAddress + "product-data/selectProductFoodData/${productId}"

                    val listenerSelectData = object : SelectProductFoodData.getDataComplete {
                        override fun getDataComplete(foodAndBevList: List<FoodAndBev>) {

                            val foodAndBev = foodAndBevList[0]
                            orderListItem.add(
                                    OrderData(
                                            foodAndBev.foodAndBevImage,
                                            foodAndBev.foodAndBevBrand,
                                            orderDetail[i].quantity,
                                            foodAndBev.foodAndBevPrice,
                                            foodAndBev.foodAndBevPrice!!  *  orderDetail[i].quantity!!
                                    )
                            )
                            val recyclerView: RecyclerView = findViewById(R.id.orderRecycleView)

                            recyclerView.apply {
                                layoutManager = LinearLayoutManager(this@OderActivity)
                                adapter = OrderAdapter(orderListItem)
                            }


                        }

                    }
                    SelectProductFoodData(listenerSelectData).execute(urlSelectData)
                } else if (categoryId == 2) {
                } else if (categoryId == 3) {
                }
            }

            OrderNo.text = order.orderId.toString()
            Price.text = "฿ " + order.totalPrice.toString()

        }

    }
}

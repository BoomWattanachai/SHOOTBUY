package com.google.firebase.ml.md.kotlin.Order

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.ml.md.R
import com.google.firebase.ml.md.kotlin.EntityModels.ProductData.FoodAndBev
import com.google.firebase.ml.md.kotlin.EntityModels.ProductData.Tile
import com.google.firebase.ml.md.kotlin.EntityModels.ProductOrder.Order
import com.google.firebase.ml.md.kotlin.IPAddress
import com.google.firebase.ml.md.kotlin.LiveObjectDetectionFragment
import com.google.firebase.ml.md.kotlin.Models.Service.ProductData.SelectProductFoodData
import com.google.firebase.ml.md.kotlin.Models.Service.ProductData.SelectProductTileData
import com.google.firebase.ml.md.kotlin.Models.Service.ProductOrder.CheckoutProductOrder
import com.google.firebase.ml.md.kotlin.Models.Service.ProductOrder.GetProductOrderByUuid
import kotlinx.android.synthetic.main.activity_oder.*
import java.text.SimpleDateFormat
import java.util.*

class OrderFragment : Fragment() {
    var orderId: Int? = null
    //    var Address: TextView? = null
//    var Name: TextView? = null
//    var date: TextView? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var v = inflater.inflate(R.layout.activity_oder, container, false)

        val pref = this.activity!!.getSharedPreferences("SP_USER_DATA", Context.MODE_PRIVATE)

        var Address = v.findViewById<TextView>(R.id.Address)
        var Name = v.findViewById<TextView>(R.id.Name)
        var date = v.findViewById<TextView>(R.id.date)
        var btnConfirm = v.findViewById<Button>(R.id.btnConfirm)

        val listenerSelectOrder = object : GetProductOrderByUuid.getDataComplete {
            override fun getDataComplete(orderList: List<Order>) {
                val order = orderList[0]
                val recyclerView: RecyclerView = v!!.findViewById(R.id.orderRecycleView)

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
                                                foodAndBev.foodAndBevModel,
                                                orderDetail[i].quantity,
                                                foodAndBev.foodAndBevPrice,
                                                foodAndBev.foodAndBevPrice!! * orderDetail[i].quantity!!
                                        )
                                )

                                recyclerView.apply {
                                    layoutManager = LinearLayoutManager(this@OrderFragment.context)
                                    adapter = OrderAdapter(orderListItem)
                                }


                            }

                        }
                        SelectProductFoodData(listenerSelectData).execute(urlSelectData)
                    } else if (categoryId == 2) {
                    } else if (categoryId == 3) {
                    } else if (categoryId == 4) {
                        val urlSelectData = IPAddress.ipAddress + "product-data/selectProductTileData/${productId}"

                        val listenerSelectData = object : SelectProductTileData.getDataComplete {
                            override fun getDataComplete(tileList: List<Tile>) {

                                val tile = tileList[0]
                                orderListItem.add(
                                        OrderData(
                                                tile.tileImage,
                                                tile.tileBrand,
                                                tile.tileModel,
                                                orderDetail[i].quantity,
                                                tile.tilePrice!!.toInt(),
                                                tile.tilePrice.toInt() * orderDetail[i].quantity!!
                                        )
                                )


                                recyclerView.apply {
                                    layoutManager = LinearLayoutManager(this@OrderFragment.context)
                                    adapter = OrderAdapter(orderListItem)
                                }


                            }

                        }
                        SelectProductTileData(listenerSelectData).execute(urlSelectData)
                    }
                }

                OrderNo.text = order.orderId.toString()
                Price.text = "฿ " + order.totalPrice.toString()

            }

        }

        var urlGetType = IPAddress.ipAddress + "product-order/getProductOrderByUuid/" + pref.getString("UUID", "")
        GetProductOrderByUuid(listenerSelectOrder).execute(urlGetType)


//        val address = intent.getStringExtra("Address")
//        val addressId = intent.getStringExtra("AddressId")
//        val fullName = intent.getStringExtra("FullName")

        val address = this.arguments!!.getString("Address", "")
        val addressId = this.arguments!!.getString("AddressId", "")
        val fullName = this.arguments!!.getString("FullName", "")

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


            (this.context as FragmentActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.fl_main, LiveObjectDetectionFragment())
                    .commit()

//            startActivity(Intent(this, LiveObjectDetectionActivity::class.java))
        }

        return v
    }
}
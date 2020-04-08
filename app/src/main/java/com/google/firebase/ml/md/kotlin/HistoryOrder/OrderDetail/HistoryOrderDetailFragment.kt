package com.google.firebase.ml.md.kotlin.HistoryOrder.OrderDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.ml.md.R
import com.google.firebase.ml.md.kotlin.EntityModels.ProductData.FoodAndBev
import com.google.firebase.ml.md.kotlin.EntityModels.ProductData.Tile
import com.google.firebase.ml.md.kotlin.EntityModels.ProductOrder.Order
import com.google.firebase.ml.md.kotlin.IPAddress
import com.google.firebase.ml.md.kotlin.Models.Service.ProductData.SelectProductFoodData
import com.google.firebase.ml.md.kotlin.Models.Service.ProductData.SelectProductTileData
import com.google.firebase.ml.md.kotlin.Models.Service.ProductOrder.GetProductOrderByOrderId
import kotlinx.android.synthetic.main.activity_order_detail.*

class HistoryOrderDetailFragment:Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var v = inflater.inflate(R.layout.activity_order_detail,container,false)



        val listenerSelectOrder = object : GetProductOrderByOrderId.getDataComplete {
            override fun getDataComplete(orderList: List<Order>) {
                val order = orderList[0]

                val orderDetail = order.orderDetail
                var orderDetailList = ArrayList<OrderDetailData>()
                val recyclerView: RecyclerView = v.findViewById(R.id.historyDetailOrderRecycleView)
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
                                                foodAndBev.foodAndBevModel,
                                                orderDetail[i].quantity,
                                                foodAndBev.foodAndBevPrice,
                                                foodAndBev.foodAndBevPrice!!  *  orderDetail[i].quantity!!
                                        )
                                )
//                            val recyclerView: RecyclerView = findViewById(R.id.historyDetailOrderRecycleView)

                                recyclerView.apply {
                                    layoutManager = LinearLayoutManager(this@HistoryOrderDetailFragment.context)
                                    adapter = OrderDetailAdapter(orderDetailList)
                                }


                            }

                        }
                        SelectProductFoodData(listenerSelectData).execute(urlSelectData)
                    } else if (categoryId == 2) {
                    } else if (categoryId == 3) {
                    }
                    else if (categoryId == 4) {
                        val urlSelectData = IPAddress.ipAddress + "product-data/selectProductTileData/${productId}"

                        val listenerSelectData = object : SelectProductTileData.getDataComplete {
                            override fun getDataComplete(tileList: List<Tile>) {

                                val tile = tileList[0]
                                orderDetailList.add(
                                        OrderDetailData(
                                                tile.tileImage,
                                                tile.tileBrand,
                                                tile.tileModel,
                                                orderDetail[i].quantity,
                                                tile.tilePrice!!.toInt(),
                                                tile.tilePrice.toInt()  *  orderDetail[i].quantity!!
                                        )
                                )


                                recyclerView.apply {
                                    layoutManager = LinearLayoutManager(this@HistoryOrderDetailFragment.context)
                                    adapter = OrderDetailAdapter(orderDetailList)
                                }


                            }

                        }
                        SelectProductTileData(listenerSelectData).execute(urlSelectData)
                    }
                }

                history_order_total_price.text = "฿" + order.totalPrice.toString()
                history_order_date.text = order.orderDateTime

            }

        }


        val orderNumber=this.arguments!!.getString("orderNumber","")
//        val orderNumber="12"

//        order_detail_order_number.text = "Order #"+orderNumber
        var order_detail_order_number = v.findViewById(R.id.order_detail_order_number) as TextView
        order_detail_order_number.text = "Order #"+orderNumber



        var urlGetDetailProductOrder = IPAddress.ipAddress + "product-order/getProductOrderByOrderId/" + orderNumber
//        var urlGetDetailProductOrder = IPAddress.ipAddress + "product-order/getProductOrderByOrderId/12"
        GetProductOrderByOrderId(listenerSelectOrder).execute(urlGetDetailProductOrder)

//        historyDetailOrderBackBtn.setOnClickListener {
//            onBackPressed()
//        }

        return v
    }


}
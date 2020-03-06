package com.google.firebase.ml.md.kotlin.Cart

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.ml.md.R
import com.google.firebase.ml.md.kotlin.Address.SelectAddressActivity
import com.google.firebase.ml.md.kotlin.IPAddress
import com.google.firebase.ml.md.kotlin.Models.Response.Response_Order
import com.google.firebase.ml.md.kotlin.Models.Service.AsyncTaskGetCart

class CartActivity : AppCompatActivity() {

    private var cartRecycleView: RecyclerView? = null
    var cartCheckoutBtn: Button? = null
    var totalPrice: TextView? = null
    var cartBackBtn: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        val pref = getSharedPreferences("SP_USER_DATA", Context.MODE_PRIVATE)

        totalPrice = findViewById(R.id.totalPrice)
        cartCheckoutBtn = findViewById(R.id.cartCheckoutBtn)
        cartBackBtn = findViewById(R.id.cartBackBtn)


        val listenerSelectCart = object : AsyncTaskGetCart.getDataComplete {
            override fun getDataComplete(responseOrderList: List<Response_Order>) {
                var responseOrder = responseOrderList[0]
                var orderDetailList = responseOrder.orderDetail
                for (i in 0..orderDetailList!!.size) {
                    var categoryId = orderDetailList[i].product!!.categoryId
                    if (categoryId == 1) {


                    } else if (categoryId == 2)
                    {

                    }else if (categoryId == 3)
                    {

                    }
                }

            }

        }

        AsyncTaskGetCart(listenerSelectCart).execute(IPAddress.ipAddress + "product-order/getProductOrderByUuid/" + pref.getString("UUID", ""))


        cartRecycleView = findViewById<RecyclerView>(R.id.cartRecycleView).apply {
            layoutManager = LinearLayoutManager(this@CartActivity)
//            adapter = CartAdapter(Cart,this@CartActivity)
        }

        cartBackBtn?.setOnClickListener {
            onBackPressed()
        }

//        cartCheckoutBtn?.setOnClickListener {
//            startActivity(Intent(this, SelectAddressActivity::class.java))
//        }
//        cartRecycleView?.adapter = CartAdapter(Cart.cartItemList, this)
        //totalPrice.text = Cart.getCartTotalPrice().toString()
    }

    fun goToSelectAddress() {
        startActivity(Intent(this, SelectAddressActivity::class.java))
    }
}

package com.google.firebase.ml.md.kotlin.Cart

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.ml.md.R
import com.google.firebase.ml.md.kotlin.Address.NewAddressActivity
import com.google.firebase.ml.md.kotlin.Address.SelectAddressActivity

class CartActivity : AppCompatActivity() {

    private var cartRecycleView: RecyclerView? = null
    var cartCheckoutBtn: Button? = null
    var totalPrice: TextView? = null
    var cartBackBtn:TextView?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        totalPrice = findViewById(R.id.totalPrice)
        cartCheckoutBtn = findViewById(R.id.cartCheckoutBtn)
        cartBackBtn=findViewById(R.id.cartBackBtn)
        cartRecycleView = findViewById<RecyclerView>(R.id.cartRecycleView).apply {
            layoutManager = LinearLayoutManager(this@CartActivity)
            adapter = CartAdapter(Cart,this@CartActivity)
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

    fun goToSelectAddress(){
        startActivity(Intent(this, SelectAddressActivity::class.java))
    }
}

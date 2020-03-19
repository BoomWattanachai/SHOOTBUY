package com.google.firebase.ml.md.kotlin.Cart

//import com.google.firebase.ml.md.kotlin.Models.Response.Response_Order
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
import com.google.firebase.ml.md.kotlin.EntityModels.ProductData.FoodAndBev
import com.google.firebase.ml.md.kotlin.EntityModels.ProductData.Tile
import com.google.firebase.ml.md.kotlin.EntityModels.ProductOrder.Order
import com.google.firebase.ml.md.kotlin.IPAddress
import com.google.firebase.ml.md.kotlin.Models.Service.ProductData.SelectProductFoodData
import com.google.firebase.ml.md.kotlin.Models.Service.ProductData.SelectProductTileData
import com.google.firebase.ml.md.kotlin.Models.Service.ProductOrder.GetProductOrderByUuid

class CartActivity : AppCompatActivity() {

    private var cartRecycleView: RecyclerView? = null
    var cartOrder: ArrayList<CartItem> = ArrayList()
    var cartCheckoutBtn: Button? = null
    var totalPrice: TextView? = null
    var cartBackBtn: TextView? = null
    //    var count:Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        val pref = getSharedPreferences("SP_USER_DATA", Context.MODE_PRIVATE)

        totalPrice = findViewById(R.id.totalPrice)
        cartCheckoutBtn = findViewById(R.id.cartCheckoutBtn)
        cartBackBtn = findViewById(R.id.cartBackBtn)

        var urlGetType = IPAddress.ipAddress + "product-order/getProductOrderByUuid/" + pref.getString("UUID", "")
        GetProductOrderByUuid(listenerSelectCart).execute(urlGetType)

        cartBackBtn?.setOnClickListener {
            onBackPressed()
        }


//        cartCheckoutBtn?.setOnClickListener {
//            startActivity(Intent(this, SelectAddressActivity::class.java))
//        }
//        cartRecycleView?.adapter = CartAdapter(Cart.cartItemList, this)
        //totalPrice.text = Cart.getCartTotalPrice().toString()
    }

    val listenerSelectCart = object : GetProductOrderByUuid.getDataComplete {
        override fun getDataComplete(orderList: List<Order>) {
            if (orderList.isNotEmpty()) {
                cartCheckoutBtn!!.isEnabled = true
                val order = orderList[0]

                val orderDetail = order.orderDetail


//            count = orderDetail!!.size

                for (i in 0 until orderDetail!!.size) {
                    val categoryId = orderDetail[i].product!!.categoryId
                    val productId = orderDetail[i].product!!.productId
                    if (categoryId == 1) {
                        val urlSelectData = IPAddress.ipAddress + "product-data/selectProductFoodData/${productId}"

                        val listenerSelectData = object : SelectProductFoodData.getDataComplete {
                            override fun getDataComplete(foodAndBevList: List<FoodAndBev>) {

                                val foodAndBev = foodAndBevList[0]
//                            count = count!!.minus(1)
                                cartOrder.add(
                                        CartItem(
                                                orderDetail[i].orderId,
                                                orderDetail[i].productId,
                                                foodAndBev.foodAndBevModel,
                                                foodAndBev.foodAndBevImage,
                                                foodAndBev.foodAndBevBrand,
                                                orderDetail[i].quantity,
                                                foodAndBev.foodAndBevPrice
                                        )
                                )

//                            if(count!! == 0)
//                            {
//                                cartRecycleView = findViewById<RecyclerView>(R.id.cartRecycleView).apply {
//                                    layoutManager = LinearLayoutManager(this@CartActivity)
//                                    adapter = CartAdapter(cartOrder, this@CartActivity)
//                                }
//                            }

                                cartRecycleView = findViewById<RecyclerView>(R.id.cartRecycleView).apply {
                                    layoutManager = LinearLayoutManager(this@CartActivity)
                                    adapter = CartAdapter(cartOrder, this@CartActivity)
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
//                            count = count!!.minus(1)
                                cartOrder.add(
                                        CartItem(
                                                orderDetail[i].orderId,
                                                orderDetail[i].productId,
                                                tile.tileModel,
                                                tile.tileImage,
                                                tile.tileBrand,
                                                orderDetail[i].quantity,
                                                tile.tilePrice!!.toInt()
                                        )
                                )
                                cartRecycleView = findViewById<RecyclerView>(R.id.cartRecycleView).apply {
                                    layoutManager = LinearLayoutManager(this@CartActivity)
                                    adapter = CartAdapter(cartOrder, this@CartActivity)
                                }


                            }

                        }
                        SelectProductTileData(listenerSelectData).execute(urlSelectData)
                    }
                }

            }else
            {
                cartCheckoutBtn!!.isEnabled = false
            }

        }

    }


    fun goToSelectAddress() {
        startActivity(Intent(this, SelectAddressActivity::class.java))
    }
}

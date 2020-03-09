package com.google.firebase.ml.md.kotlin.Cart

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.ml.md.R
import com.google.firebase.ml.md.kotlin.EntityModels.ProductOrder.OrderDetail
import com.google.firebase.ml.md.kotlin.IPAddress
import com.google.firebase.ml.md.kotlin.Models.Service.ProductOrder.DecreaseOrderDetailQuantity
import com.google.firebase.ml.md.kotlin.Models.Service.ProductOrder.IncreaseOrderDetailQuantity
import com.squareup.picasso.Picasso
import java.text.NumberFormat

class CartAdapter( var cart: ArrayList<CartItem>, var cartActivity: CartActivity) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {
    class CartViewHolder private constructor(view: View) : RecyclerView.ViewHolder(view) {
        private val productImage: ImageView = view.findViewById(R.id.product_image)
        private val productName: TextView = view.findViewById(R.id.product_name)
        private val productQuantity: TextView = view.findViewById(R.id.product_quantity)
        val cartItemRemove: Button = view.findViewById(R.id.cartItemRemove)
        private val productPrice: TextView = view.findViewById(R.id.product_price)
        val cartItemIncrease: ImageButton = view.findViewById(R.id.cart_item_increase)
        val cartItemDecrease: ImageButton = view.findViewById(R.id.cart_item_decrease)
        fun bindProduct(cartItem: CartItem) {
//            productImage.setImageResource(cartItem.imageResource)
            Picasso.get().load(cartItem.imageResource).into(productImage)
            productName.text = cartItem.nameData+" "
            productQuantity.text = cartItem.amount.toString()
            productPrice.text = "$"+NumberFormat.getInstance().format(cartItem.price).toString()


        }




        companion object {
            fun create(parent: ViewGroup) =
                    CartViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cart_item, parent, false))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder =
            CartViewHolder.create(parent)


    override fun getItemCount(): Int = cart.size

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {

        cartActivity.totalPrice?.text = Cart.getCartTotalPrice(cart).toString()

        holder.cartItemIncrease.setOnClickListener {

//            cart.cartItemList[position].increaseQuantity()
//                cart[position].amount!!.inc()

            cart[position].amount =  cart[position].amount!!.plus(1)
            cartActivity.totalPrice?.text = Cart.getCartTotalPrice(cart).toString()

            var urlIncress = IPAddress.ipAddress + "product-order/increaseOrderDetailQuantity/"
            IncreaseOrderDetailQuantity(OrderDetail(cart[position].orderId,null,cart[position].productId,null,null)).execute(urlIncress)


            notifyDataSetChanged()
        }
        holder.cartItemDecrease.setOnClickListener {
//            cart.cartItemList[position].decreaseQuantity()
            if(cart[position].amount!! > 1)
            {
                cart[position].amount = cart[position].amount!!.minus(1)
                cartActivity.totalPrice?.text = Cart.getCartTotalPrice(cart).toString()

                var urlIncress = IPAddress.ipAddress + "product-order/decreaseOrderDetailQuantity/"
                DecreaseOrderDetailQuantity(OrderDetail(cart[position].orderId,null,cart[position].productId,null,null)).execute(urlIncress)
            }



            notifyDataSetChanged()

        }
//        holder.cartItemRemove.setOnClickListener {
//            cart.cartItemList.removeAt(position)
//            if(cart.cartItemList.size <= 0) cartActivity.onBackPressed()
//            notifyDataSetChanged()
//        }

        cartActivity.cartCheckoutBtn?.setOnClickListener {
//            cartActivity.onBackPressed()
//            cart.cartItemList.clear()
//            cartActivity.totalPrice?.text = "$"+ NumberFormat.getInstance().format(cart.getCartTotalPrice()).toString()
            cartActivity.goToSelectAddress()
//            startActivity(Intent(this, NewAddressActivity::class.java))
//            notifyDataSetChanged()
//            NumberFormat.getInstance().format(cart.getCartTotalPrice())
        }
//        cartActivity.totalPrice?.text = "$"+ NumberFormat.getInstance().format(cart.getCartTotalPrice()).toString()

        holder.bindProduct(cart[position])

    }


}
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
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.electronic_layout.*
import java.text.NumberFormat

class CartAdapter(private var cart: Cart.Companion, var cartActivity: CartActivity) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {
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


    override fun getItemCount(): Int = cart.cartItemList.size

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {

        holder.cartItemIncrease.setOnClickListener {
            cart.cartItemList[position].increaseQuantity()
//            cartActivity.totalPrice?.text = Cart.getCartTotalPrice().toString()
            notifyDataSetChanged()
        }
        holder.cartItemDecrease.setOnClickListener {
            cart.cartItemList[position].decreaseQuantity()
//            cartActivity.totalPrice?.text = Cart.getCartTotalPrice().toString()
            notifyDataSetChanged()

        }
        holder.cartItemRemove.setOnClickListener {
            cart.cartItemList.removeAt(position)
            if(cart.cartItemList.size <= 0) cartActivity.onBackPressed()
            notifyDataSetChanged()
        }
        cartActivity.cartCheckoutBtn?.setOnClickListener {
            cartActivity.onBackPressed()
            cart.cartItemList.clear()
            cartActivity.totalPrice?.text = "$"+ NumberFormat.getInstance().format(cart.getCartTotalPrice()).toString()
//            notifyDataSetChanged()
//            NumberFormat.getInstance().format(cart.getCartTotalPrice())
        }
        cartActivity.totalPrice?.text = "$"+ NumberFormat.getInstance().format(cart.getCartTotalPrice()).toString()
        holder.bindProduct(cart.cartItemList[position])

    }


}
package com.google.firebase.ml.md.kotlin.Cart

class Cart {
    companion object {
        var cartItemList:ArrayList<CartItem> = ArrayList()
        fun addItem(cartItem:CartItem) {
            cartItemList.add(cartItem)
        }
        fun getCartTotalPrice(cart:ArrayList<CartItem>):Int{
            var cartTotalPrice:Int=0
            for(cartItem in cart){
                cartTotalPrice+=cartItem.amount!! * cartItem.price!!
            }
            return cartTotalPrice
        }

    }
}

//data class Cart (var imageResource:String,var nameData:String,var amount:Int,var price:Int)
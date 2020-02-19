package com.google.firebase.ml.md.kotlin.Cart

class CartItem(var imageResource:String,var nameData:String,var amount:Int,var price:Int) {
    fun increaseQuantity(){
        this.amount++
    }
    fun decreaseQuantity(){
        if(amount > 1)
            amount--
    }
}
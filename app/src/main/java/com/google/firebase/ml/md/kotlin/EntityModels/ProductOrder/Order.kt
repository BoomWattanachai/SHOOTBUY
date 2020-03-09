package com.google.firebase.ml.md.kotlin.EntityModels.ProductOrder

import com.google.firebase.ml.md.kotlin.EntityModels.UserData.User

data class Order (val orderId:Int?,val uuid:String?,val user:User?,val orderDateTime:String?,val totalPrice:Int?,
                  val orderStatus:Int?,val orderDetail:List<OrderDetail>?){
}
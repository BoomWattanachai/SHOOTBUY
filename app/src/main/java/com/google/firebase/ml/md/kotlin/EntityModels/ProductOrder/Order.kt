package com.google.firebase.ml.md.kotlin.EntityModels.ProductOrder

import com.google.firebase.ml.md.kotlin.EntityModels.UserData.User

data class Order (var orderId:Int? = null,var uuid:String?= null,var user:User?= null,var orderDateTime:String?= null,var totalPrice:Int?= null,
                  var orderStatus:Int?= null,var orderDetail:List<OrderDetail>?= null,var addressId:Int? = null){
}
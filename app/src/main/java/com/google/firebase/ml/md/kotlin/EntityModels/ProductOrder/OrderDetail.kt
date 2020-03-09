package com.google.firebase.ml.md.kotlin.EntityModels.ProductOrder

import com.google.firebase.ml.md.kotlin.EntityModels.ProductData.Product

data class OrderDetail (val orderId:Int?,val order:Order?,val productId:String?,val product:Product?,val quantity:Int?){
}
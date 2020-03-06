package com.google.firebase.ml.md.kotlin.Models.Request

data class Request_AddCart (val uuid:String?,val orderDetail:Array<ProductId>?){
}

data class ProductId (val productId:String?)
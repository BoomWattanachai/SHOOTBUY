package com.google.firebase.ml.md.kotlin.EntityModels.UserData

import com.google.firebase.ml.md.kotlin.EntityModels.ProductData.Product

data class ScanHistory (val uuid:String?, val user: User?, val productId:String?, val product: Product?,
                        val scanDateTime:String?){
}
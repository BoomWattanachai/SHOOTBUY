package com.google.firebase.ml.md.kotlin.EntityModels.ProductData

import com.google.firebase.ml.md.kotlin.EntityModels.UserData.ScanHistory

data class Product (val productId:String?, val categoryId:Int?, val category:Category?,
                    val scanHistory:List<ScanHistory>?){
}


package com.google.firebase.ml.md.kotlin.EntityModels.ProductData

data class Furniture (val furnitureId: String?, val furnitureBrand: String?, val furnitureModel: String?,
                      val furnitureImage: String?, val furniturePrice: Int?, val furnitureSize: String?,
                      val furnitureDetail:String?, val furnitureAmount: Int?, val color: List<Color>?) {
}


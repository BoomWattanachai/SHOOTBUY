package com.google.firebase.ml.md.kotlin.EntityModels.ProductData

data class Electronic(val electronicId: String?, val electronicBrand: String?, val electronicModel: String?,
                      val electronicImage: String?, val electronicPrice: Int?, val electronicSpec: String?,
                      val electronicAmount: Int?, val color: List<Color>) {
}
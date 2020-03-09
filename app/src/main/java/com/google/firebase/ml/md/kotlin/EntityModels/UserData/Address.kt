package com.google.firebase.ml.md.kotlin.EntityModels.UserData

data class Address (val addressId:Int?,val uuid:String?,val addressNumber:String?,val district:String?,
                    val subDistrict:String?,val province:String?,val zipCode:String?,val user:User?){
}
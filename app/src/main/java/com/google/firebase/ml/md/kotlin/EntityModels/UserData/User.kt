package com.google.firebase.ml.md.kotlin.EntityModels.UserData

data class User (val uuid:String?, val email:String?, val firstName:String?, val lastName:String?,
                 val address:List<Address>?,val scanHistory:List<ScanHistory>?){
}
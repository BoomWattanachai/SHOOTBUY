package com.google.firebase.ml.md.kotlin.EntityModels.UserData

data class User(var uuid: String? = null, var email: String? = null, var firstName: String? = null, var lastName: String? = null,
                var address: List<Address>? = null, var scanHistory: List<ScanHistory>? = null) {
}
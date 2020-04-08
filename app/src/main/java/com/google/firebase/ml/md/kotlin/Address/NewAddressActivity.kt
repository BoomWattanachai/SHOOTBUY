package com.google.firebase.ml.md.kotlin.Address

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.ml.md.R
import com.google.firebase.ml.md.kotlin.EntityModels.UserData.Address
import com.google.firebase.ml.md.kotlin.IPAddress
import com.google.firebase.ml.md.kotlin.Models.Service.ProductOrder.InsertProductOrder
import com.google.firebase.ml.md.kotlin.Models.Service.UserData.InsertUserAddress
import kotlinx.android.synthetic.main.activity_new_address.*

class NewAddressActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_address)

        val pref = getSharedPreferences("SP_USER_DATA", Context.MODE_PRIVATE)

        btnNewAddress.setOnClickListener {
            var address = Address(
                    null,
                    pref.getString("UUID", ""),
                    inputHouseNo.text.toString(),
                    inputDistrict.text.toString(),
                    inputSubDistrict.text.toString(),
                    inputProvince.text.toString(),
                    inputZipCode.text.toString(),
                    null
            )
            InsertUserAddress(address).execute(IPAddress.ipAddress + "user-data/insertUserAddress/")

            startActivity(Intent(this, SelectAddressActivity::class.java))
        }


    }
}

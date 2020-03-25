package com.google.firebase.ml.md.kotlin.Address

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.firebase.ml.md.R
import com.google.firebase.ml.md.kotlin.EntityModels.UserData.Address
import com.google.firebase.ml.md.kotlin.IPAddress
import com.google.firebase.ml.md.kotlin.Models.Service.UserData.InsertUserAddress
import kotlinx.android.synthetic.main.activity_new_address.*

class NewAddressFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var v = inflater.inflate(R.layout.activity_new_address, container, false)

        val pref = this.activity!!.getSharedPreferences("SP_USER_DATA", Context.MODE_PRIVATE)
        var btnNewAddress = v!!.findViewById(R.id.btnNewAddress) as Button



        btnNewAddress.setOnClickListener {
            if (inputHouseNo.text.toString() != "" && inputDistrict.text.toString() != "" && inputSubDistrict.text.toString() != "" && inputProvince.text.toString() != "" && inputZipCode.text.toString() != "") {
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
                
                (this.context as FragmentActivity).supportFragmentManager.popBackStack()
//                (this.context as FragmentActivity).supportFragmentManager.beginTransaction()
//                        .replace(R.id.fl_main, SelectAddressFragment())
//                        .commit()
            }


//            startActivity(Intent(this, SelectAddressActivity::class.java))
        }

        return v
    }
}
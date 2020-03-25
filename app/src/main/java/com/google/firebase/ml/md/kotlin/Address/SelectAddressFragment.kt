package com.google.firebase.ml.md.kotlin.Address

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.relativeui.Address.AddressAdapter
import com.example.relativeui.Address.AddressData
import com.google.firebase.ml.md.R
import com.google.firebase.ml.md.kotlin.EntityModels.UserData.User
import com.google.firebase.ml.md.kotlin.IPAddress
import com.google.firebase.ml.md.kotlin.Models.Service.UserData.GetUserAddressByUuid
import kotlinx.android.synthetic.main.activity_select_address.*

class SelectAddressFragment:Fragment() {
    var btnCheckout: Button? = null
    var btnNewAddress: Button? = null
    var fullName:String? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var v = inflater.inflate(R.layout.activity_select_address,container,false)

        btnCheckout = v!!.findViewById(R.id.btnCheckout)
        btnNewAddress = v!!.findViewById(R.id.btnNewAddress)

        val pref = this.activity!!.getSharedPreferences("SP_USER_DATA", Context.MODE_PRIVATE)


        var urlGetType = IPAddress.ipAddress + "user-data/getUserAddressByUuid/" + pref.getString("UUID", "")

        val listenerSelectData = object : GetUserAddressByUuid.getDataComplete {
            override fun getDataComplete(userList: List<User>) {
                var user = userList[0]
                fullName = user.firstName + " " + user.lastName
                var addressArray = user.address
                var addressList = ArrayList<AddressData>()

                (0 until (addressArray!!.size)).forEach { i ->
                    var address = addressArray[i].addressNumber.toString() + " " + addressArray[i].subDistrict + " " +
                            addressArray[i].district + " " + addressArray[i].province + " " + addressArray[i].zipCode
                    addressList.add(
                            AddressData(
                                    addressArray[i].addressId,
                                    address,
                                    false
                            )
                    )
                }
                val recyclerView: RecyclerView = v.findViewById(R.id.cartRecycleView)

                recyclerView.apply {
                    layoutManager = LinearLayoutManager(this@SelectAddressFragment.context)
                    adapter = AddressAdapter(addressList, this@SelectAddressFragment, this@SelectAddressFragment.context!!)
                }
            }
        }

        GetUserAddressByUuid(listenerSelectData).execute(urlGetType)

//        val data = arrayOf(
//                "224 หมู่ ที่ 10, ตำบล สะเดา อำเภอนางรอง บุรีรัมย์",
//                "1034 ตำบล ปงยางคก อำเภอห้างฉัตร ลำปาง 52190",
//                "188 111 ซอย จันทร์สม 3 ตำบล ท่าตะเภา อำเภอเมืองชุมพร ชุมพร 86000",
//                "188 111 ซอย จันทร์สม 3 ตำบล ท่าตะเภา อำเภอเมืองชุมพร ชุมพร 86000",
//                "188 111 ซอย จันทร์สม 3 ตำบล ท่าตะเภา อำเภอเมืองชุมพร ชุมพร 86000",
//                "188 111 ซอย จันทร์สม 3 ตำบล ท่าตะเภา อำเภอเมืองชุมพร ชุมพร 86000",
//                "188 111 ซอย จันทร์สม 3 ตำบล ท่าตะเภา อำเภอเมืองชุมพร ชุมพร 86000",
//                "188 111 ซอย จันทร์สม 3 ตำบล ท่าตะเภา อำเภอเมืองชุมพร ชุมพร 86000",
//                "188 111 ซอย จันทร์สม 3 ตำบล ท่าตะเภา อำเภอเมืองชุมพร ชุมพร 86000",
//                "188 111 ซอย จันทร์สม 3 ตำบล ท่าตะเภา อำเภอเมืองชุมพร ชุมพร 86000",
//                "188 111 ซอย จันทร์สม 3 ตำบล ท่าตะเภา อำเภอเมืองชุมพร ชุมพร 86000",
//                "188 111 ซอย จันทร์สม 3 ตำบล ท่าตะเภา อำเภอเมืองชุมพร ชุมพร 86000",
//                "188 111 ซอย จันทร์สม 3 ตำบล ท่าตะเภา อำเภอเมืองชุมพร ชุมพร 86000",
//                "188 111 ซอย จันทร์สม 3 ตำบล ท่าตะเภา อำเภอเมืองชุมพร ชุมพร 86000",
//                "188 111 ซอย จันทร์สม 3 ตำบล ท่าตะเภา อำเภอเมืองชุมพร ชุมพร 86000",
//                "188 111 ซอย จันทร์สม 3 ตำบล ท่าตะเภา อำเภอเมืองชุมพร ชุมพร 86000"
//        )
//
//        var addressList = ArrayList<AddressData>()
//
//        (0 until (data.size)).forEach { i ->
//            addressList.add(
//                    AddressData(
//                            data[i],
//                            false
//                    )
//            )
//        }
//
//
//        val recyclerView: RecyclerView = findViewById(R.id.cartRecycleView)
//
//        recyclerView.apply {
//            layoutManager = LinearLayoutManager(this@SelectAddressActivity)
//            adapter = AddressAdapter(addressList,this@SelectAddressActivity,this@SelectAddressActivity)
//        }



        btnNewAddress!!.setOnClickListener {
            (this.context as FragmentActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.fl_main, NewAddressFragment())
                    .commit()
//            startActivity(Intent(this, NewAddressActivity::class.java))
//            startActivity(intent)
        }


        return v
    }
}
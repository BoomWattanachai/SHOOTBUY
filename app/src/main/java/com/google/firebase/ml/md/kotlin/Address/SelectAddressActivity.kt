package com.google.firebase.ml.md.kotlin.Address

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.relativeui.Address.AddressAdapter
import com.example.relativeui.Address.AddressData
import com.google.firebase.ml.md.R
import com.google.firebase.ml.md.kotlin.EntityModels.UserData.User
import com.google.firebase.ml.md.kotlin.IPAddress
import com.google.firebase.ml.md.kotlin.Models.Service.UserData.GetUserAddressByUuid
import kotlinx.android.synthetic.main.activity_select_address.*

class SelectAddressActivity : AppCompatActivity() {

    var btnCheckout: Button? = null
    var fullName:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_address)

        btnCheckout = findViewById(R.id.btnCheckout)

        val pref = getSharedPreferences("SP_USER_DATA", Context.MODE_PRIVATE)


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
                val recyclerView: RecyclerView = findViewById(R.id.cartRecycleView)

//                recyclerView.apply {
//                    layoutManager = LinearLayoutManager(this@SelectAddressActivity)
//                    adapter = AddressAdapter(addressList, this@SelectAddressActivity, this@SelectAddressActivity)
//                }
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


        btnNewAddress.setOnClickListener {
            startActivity(Intent(this, NewAddressActivity::class.java))
//            startActivity(intent)
        }

//        btnCheckout.setOnClickListener {
////            startActivity(Intent(this, OderActivity::class.java))
//            val intent = Intent(this, OderActivity::class.java)
//            var test = radio.text
//            intent.putExtra("Address","aaaa")
//            startActivity(intent)
//
//        }
    }
}

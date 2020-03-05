package com.google.firebase.ml.md.kotlin.HistoryOrder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.ml.md.R
import kotlinx.android.synthetic.main.activity_history_order.*
import kotlinx.android.synthetic.main.activity_order_detail.*

class HistoryOrderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_order)

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

        var historyOrderList = ArrayList<HistoryOrderData>()

        (0 until 15).forEach { i ->
            historyOrderList.add(
                    HistoryOrderData(
                            "00000"+i,
                            (0..1).random()
                    )
            )
        }


        val recyclerView: RecyclerView = findViewById(R.id.historyOrderRecycleView)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@HistoryOrderActivity)
            adapter = HistoryOrderAdapter(historyOrderList,this@HistoryOrderActivity,this@HistoryOrderActivity)
        }

        historyOrderBackBtn.setOnClickListener {
            onBackPressed()
        }
    }
}

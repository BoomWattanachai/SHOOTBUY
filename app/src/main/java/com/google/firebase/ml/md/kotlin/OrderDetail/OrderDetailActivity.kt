package com.google.firebase.ml.md.kotlin.OrderDetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.ml.md.R
import kotlinx.android.synthetic.main.activity_order_detail.*
import java.time.LocalDate
import java.time.LocalDateTime

class OrderDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_detail)
        val orderNumber=intent.getStringExtra("orderNumber")

        order_detail_order_number.text = orderNumber


        var orderDetailList = ArrayList<OrderDetailData>()

        (0 until 10).forEach { i ->
            orderDetailList.add(
                    OrderDetailData(
                            "https://backend.tops.co.th/media/catalog/product/8/8/8851959132074_1.jpg",
                            "Coke",
                            2,
                            15
                    )
            )
        }

        val recyclerView: RecyclerView = findViewById(R.id.historyDetailOrderRecycleView)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@OrderDetailActivity)
            adapter = OrderDetailAdapter(orderDetailList)
        }

        historyDetailOrderBackBtn.setOnClickListener {
            onBackPressed()
        }

        history_order_date.text = LocalDate.now().toString()


    }
}

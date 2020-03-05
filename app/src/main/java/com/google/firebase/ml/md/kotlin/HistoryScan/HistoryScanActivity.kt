package com.google.firebase.ml.md.kotlin.HistoryScan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.ml.md.R
import kotlinx.android.synthetic.main.activity_history_scan.*

class HistoryScanActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_scan)



        var historyScanList = ArrayList<HistoryScanData>()

        (0 until 10).forEach { i ->
            historyScanList.add(
                    HistoryScanData(
                            "https://backend.tops.co.th/media/catalog/product/8/8/8851959132074_1.jpg",
                            "Coke",
                            "05/03/2020",
                            15
                    )
            )
        }


        val recyclerView: RecyclerView = findViewById(R.id.historyScanRecycleView)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@HistoryScanActivity)
            adapter = HistoryScanAdapter(historyScanList)
        }

        historyScanBackBtn.setOnClickListener {
            onBackPressed()
        }

    }
}

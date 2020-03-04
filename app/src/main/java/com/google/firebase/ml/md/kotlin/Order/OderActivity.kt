package com.google.firebase.ml.md.kotlin.Order

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.ml.md.R
import com.google.firebase.ml.md.kotlin.LiveObjectDetectionActivity
import kotlinx.android.synthetic.main.activity_oder.*


class OderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_oder)



        val data = arrayOf(
                "https://notebookspec.com/nbs/upload_notebook/20180424-193219_c.jpg",
                "https://notebookspec.com/nbs/upload_notebook/20180424-193219_c.jpg",
                "https://notebookspec.com/nbs/upload_notebook/20180424-193219_c.jpg",
                "https://notebookspec.com/nbs/upload_notebook/20180424-193219_c.jpg",
                "https://notebookspec.com/nbs/upload_notebook/20180424-193219_c.jpg",
                "https://notebookspec.com/nbs/upload_notebook/20180424-193219_c.jpg",
                "https://notebookspec.com/nbs/upload_notebook/20180424-193219_c.jpg",
                "https://notebookspec.com/nbs/upload_notebook/20180424-193219_c.jpg",
                "https://notebookspec.com/nbs/upload_notebook/20180424-193219_c.jpg",
                "https://notebookspec.com/nbs/upload_notebook/20180424-193219_c.jpg",
                "https://notebookspec.com/nbs/upload_notebook/20180424-193219_c.jpg",
                "https://notebookspec.com/nbs/upload_notebook/20180424-193219_c.jpg",
                "https://notebookspec.com/nbs/upload_notebook/20180424-193219_c.jpg",
                "https://notebookspec.com/nbs/upload_notebook/20180424-193219_c.jpg"
        )

        var orderList = ArrayList<OrderData>()

        (0 until (data.size)).forEach { i ->
            orderList.add(
                    OrderData(
                            data[i],
                            "ACER NITRO5 AN515-52",
                            2,
                            19000
                    )
            )
        }

        val address=intent.getStringExtra("Address")


        Address.text = address


        val recyclerView: RecyclerView = findViewById(R.id.orderRecycleView)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@OderActivity)
            adapter = OrderAdapter(orderList)
        }

        btnConfirm.setOnClickListener {
            startActivity(Intent(this, LiveObjectDetectionActivity::class.java))
        }
    }
}

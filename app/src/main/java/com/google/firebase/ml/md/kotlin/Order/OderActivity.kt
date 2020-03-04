package com.google.firebase.ml.md.kotlin.Order

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.ml.md.R
import com.google.firebase.ml.md.kotlin.LiveObjectDetectionActivity
import kotlinx.android.synthetic.main.activity_oder.*

class OderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_oder)

        btnConfirm.setOnClickListener {
            startActivity(Intent(this, LiveObjectDetectionActivity::class.java))
        }
    }
}

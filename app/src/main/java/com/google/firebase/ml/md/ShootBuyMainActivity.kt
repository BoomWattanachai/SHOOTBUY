package com.google.firebase.ml.md

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.ml.md.kotlin.Cart.CartFragment
import com.google.firebase.ml.md.kotlin.HistoryOrder.HistoryOrderFragment
import com.google.firebase.ml.md.kotlin.HistoryScan.HistoryScanFragment
import com.google.firebase.ml.md.kotlin.LiveObjectDetectionFragment
import com.google.firebase.ml.md.kotlin.LoginActivity
import kotlinx.android.synthetic.main.activity_shoot_buy_main.*


class ShootBuyMainActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when(item.itemId)
        {
            R.id.detect -> {
                gotoDetail(LiveObjectDetectionFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.historyOrder -> {
//                supportFragmentManager
//                        .beginTransaction()
//                        .replace(R.id.fl_main, HistoryOrderFragment())
//                        .commit()
                gotoDetail(HistoryOrderFragment())
//                Toast.makeText(this, "sms", Toast.LENGTH_SHORT).show()
                return@OnNavigationItemSelectedListener true
            }
            R.id.historyScan -> {
                gotoDetail(HistoryScanFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.cart -> {
                gotoDetail(CartFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.log_out -> {
                val intent = Intent(this, LoginActivity::class.java)
//            Log.d("Address",oldHolder!!.address.text.toString())
                intent.putExtra("Logout", "Logout")
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    fun gotoDetail(fragment: Fragment){
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fl_main, fragment)
                .commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shoot_buy_main)
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        if(savedInstanceState == null)
        {
//            gotoDetail(LiveObjectDetectionFragment())
            bottomNavigation.selectedItemId = R.id.detect
        }
//        gotoDetail(LiveObjectDetectionFragment())


    }
}

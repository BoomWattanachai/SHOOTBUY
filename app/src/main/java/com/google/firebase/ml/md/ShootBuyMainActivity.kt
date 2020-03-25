package com.google.firebase.ml.md

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.ml.md.kotlin.Cart.CartFragment
import com.google.firebase.ml.md.kotlin.HistoryOrder.HistoryOrderFragment
import com.google.firebase.ml.md.kotlin.HistoryScan.HistoryScanFragment
import com.google.firebase.ml.md.kotlin.LiveObjectDetectionFragment
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
                gotoDetail(HistoryScanFragment())
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
        gotoDetail(LiveObjectDetectionFragment())
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }
}

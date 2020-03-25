package com.google.firebase.ml.md

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.ml.md.kotlin.Cart.CartFragment
import com.google.firebase.ml.md.kotlin.HistoryOrder.HistoryOrderFragment
import com.google.firebase.ml.md.kotlin.HistoryScan.HistoryScanFragment
import com.google.firebase.ml.md.kotlin.LiveObjectDetectionFragment
import com.google.firebase.ml.md.kotlin.LoginActivity
import kotlinx.android.synthetic.main.activity_shoot_buy_main.*


class ShootBuyMainActivity : AppCompatActivity() {
    private val TAG_FRAGMENT = "TAG_FRAGMENT"

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
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
//                val intent = Intent(this, LoginActivity::class.java)
////            Log.d("Address",oldHolder!!.address.text.toString())
//                intent.putExtra("Logout", "Logout")
//                startActivity(intent)

                showDialodAnimation(R.style.DialogSlide, "Are you sure?", 0)

//                Log.d("countBack", supportFragmentManager.backStackEntryCount.toString())
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun showDialodAnimation(type: Int, message: String, num: Int) {
//        var dialog = AlertDialog.Builder(this).create()
        lateinit var dialog: AlertDialog
        if (num == 0) {
//            dialog.setTitle("Logout")
//            dialog.setMessage(message)
//            dialog.window!!.attributes.windowAnimations = type
//            dialog.show()
            // Late initialize an alert dialog object


            // Initialize a new instance of alert dialog builder object
            val builder = AlertDialog.Builder(this)

            // Set a title for alert dialog
            builder.setTitle("Logout")

            // Set a message for alert dialog
            builder.setMessage("Are you sure?")


            // On click listener for dialog buttons
            val dialogClickListener = DialogInterface.OnClickListener { _, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        val intent = Intent(this, LoginActivity::class.java)
////            Log.d("Address",oldHolder!!.address.text.toString())
                        intent.putExtra("Logout", "Logout")
                        startActivity(intent)
                    }
                    DialogInterface.BUTTON_NEGATIVE -> dialog.dismiss()
                }
            }


            // Set the alert dialog positive/yes button
            builder.setPositiveButton("YES", dialogClickListener)

            // Set the alert dialog negative/no button
            builder.setNegativeButton("NO", dialogClickListener)

            // Set the alert dialog neutral/cancel button
//            builder.setNeutralButton("CANCEL", dialogClickListener)


            // Initialize the AlertDialog using builder object
            dialog = builder.create()

            // Finally, display the alert dialog
            dialog.show()
        } else {
            dialog.dismiss()
        }
        Log.d("Boom", "show: " + dialog.isShowing)

    }

    fun gotoDetail(fragment: Fragment) {
        var name: String? = null
        if (fragment is LiveObjectDetectionFragment) {
            name = "MainDetect"
        }
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fl_main, fragment, TAG_FRAGMENT)
                .addToBackStack(name)
                .commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shoot_buy_main)
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        if (savedInstanceState == null) {
//            gotoDetail(LiveObjectDetectionFragment())
            bottomNavigation.selectedItemId = R.id.detect
        }
//        gotoDetail(LiveObjectDetectionFragment())


    }

    override fun onBackPressed() {
        var fragment = supportFragmentManager.findFragmentById(R.id.fl_main)
        if (fragment is HistoryScanFragment || fragment is HistoryOrderFragment || fragment is CartFragment) {
            supportFragmentManager.popBackStack("MainDetect", FragmentManager.POP_BACK_STACK_INCLUSIVE)
            bottomNavigation.selectedItemId = R.id.detect
        } else if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }
}

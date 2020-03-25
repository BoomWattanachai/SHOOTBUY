package com.google.firebase.ml.md.kotlin.HistoryScan

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.ml.md.R
import com.google.firebase.ml.md.kotlin.EntityModels.ProductData.FoodAndBev
import com.google.firebase.ml.md.kotlin.EntityModels.ProductData.Tile
import com.google.firebase.ml.md.kotlin.EntityModels.UserData.User
import com.google.firebase.ml.md.kotlin.IPAddress
import com.google.firebase.ml.md.kotlin.Models.Service.ProductData.SelectProductFoodData
import com.google.firebase.ml.md.kotlin.Models.Service.ProductData.SelectProductTileData
import com.google.firebase.ml.md.kotlin.Models.Service.UserData.GetUserDataScanHistory
import kotlinx.android.synthetic.main.activity_history_scan.*

class HistoryScanFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var v = inflater.inflate(R.layout.activity_history_scan,container,false)
        val listenerUserDataScanHistory = object : GetUserDataScanHistory.getDataComplete {
            override fun getDataComplete(userList: List<User>) {

                val user = userList[0]
                val scanHistoryList = user.scanHistory
                var historyScanList = ArrayList<HistoryScanData>()
                val recyclerView: RecyclerView = v.findViewById(R.id.historyScanRecycleView)


//            val orderDetail = order.orderDetail


//            count = orderDetail!!.size

                for (i in 0 until scanHistoryList!!.size) {

                    val categoryId = scanHistoryList[i].product!!.categoryId
                    val productId = scanHistoryList[i].product!!.productId
                    if (categoryId == 1) {
                        val urlSelectData = IPAddress.ipAddress + "product-data/selectProductFoodData/${productId}"

                        val listenerSelectData = object : SelectProductFoodData.getDataComplete {
                            override fun getDataComplete(foodAndBevList: List<FoodAndBev>) {

                                val foodAndBev = foodAndBevList[0]
//                            count = count!!.minus(1)
                                historyScanList.add(
                                        HistoryScanData(
                                                foodAndBev.foodAndBevImage,
                                                foodAndBev.foodAndBevBrand,
                                                foodAndBev.foodAndBevModel,
                                                scanHistoryList[i].scanDateTime,
                                                foodAndBev.foodAndBevPrice
                                        )
                                )



                                recyclerView.apply {
                                    layoutManager = LinearLayoutManager(this@HistoryScanFragment.context)
                                    adapter = HistoryScanAdapter(historyScanList)
                                }


                            }

                        }
                        SelectProductFoodData(listenerSelectData).execute(urlSelectData)
                    } else if (categoryId == 2) {
                    } else if (categoryId == 3) {
                    } else if (categoryId == 4) {
                        val urlSelectData = IPAddress.ipAddress + "product-data/selectProductTileData/${productId}"

                        val listenerSelectData = object : SelectProductTileData.getDataComplete {
                            override fun getDataComplete(tileList: List<Tile>) {

                                val tile = tileList[0]
//                            count = count!!.minus(1)
                                historyScanList.add(
                                        HistoryScanData(
                                                tile.tileImage,
                                                tile.tileBrand,
                                                tile.tileModel,
                                                scanHistoryList[i].scanDateTime,
                                                tile.tilePrice!!.toInt()
                                        )
                                )



                                recyclerView.apply {
                                    layoutManager = LinearLayoutManager(this@HistoryScanFragment.context)
                                    adapter = HistoryScanAdapter(historyScanList)
                                }


                            }

                        }
                        SelectProductTileData(listenerSelectData).execute(urlSelectData)
                    }
                }


            }

        }



        val pref = this.activity!!.getSharedPreferences("SP_USER_DATA", Context.MODE_PRIVATE)

        var urlGetUserDataScanHistory = IPAddress.ipAddress + "user-data/getUserDataScanHistory/" + pref.getString("UUID", "")
        GetUserDataScanHistory(listenerUserDataScanHistory).execute(urlGetUserDataScanHistory)



//        historyScanBackBtn.setOnClickListener {
//            onBackPressed()
//        }



        return  v
    }
}
/*
 * Copyright 2019 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.firebase.ml.md.kotlin.productSearch

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.ml.md.kotlin.EntityModels.ProductData.Electronic
import com.google.firebase.ml.md.kotlin.EntityModels.ProductData.FoodAndBev
import com.google.firebase.ml.md.kotlin.EntityModels.ProductData.Furniture
import com.google.firebase.ml.md.kotlin.EntityModels.ProductData.Product
import com.google.firebase.ml.md.kotlin.EntityModels.UserData.ScanHistory
import com.google.firebase.ml.md.kotlin.IPAddress
import com.google.firebase.ml.md.kotlin.Models.Service.ProductData.SelectProductElectronic
import com.google.firebase.ml.md.kotlin.Models.Service.ProductData.SelectProductFoodData
import com.google.firebase.ml.md.kotlin.Models.Service.ProductData.SelectProductFurniture
import com.google.firebase.ml.md.kotlin.Models.Service.ProductData.SelectProductType
import com.google.firebase.ml.md.kotlin.Models.Service.UserData.InsertUserDataScanHistory
import com.google.firebase.ml.md.kotlin.objectdetection.DetectedObject
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.automl.FirebaseAutoMLLocalModel
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.label.FirebaseVisionOnDeviceAutoMLImageLabelerOptions
import org.json.JSONArray
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/** A fake search engine to help simulate the complete work flow.  */
class SearchEngine(var context: Context) {

    private val searchRequestQueue: RequestQueue = Volley.newRequestQueue(context)
    private val requestCreationExecutor: ExecutorService = Executors.newSingleThreadExecutor()
    fun search(
            detectedObject: DetectedObject,
            listener: (detectedObject: DetectedObject, productTest: Any?) -> Unit
    ) {

        val localModel = FirebaseAutoMLLocalModel.Builder()
//                .setAssetFilePath("Models/manifest.json")
                .setAssetFilePath("3Model/manifest.json")
                .build()

        val options = FirebaseVisionOnDeviceAutoMLImageLabelerOptions.Builder(localModel)
                .setConfidenceThreshold(0.8f)  // Evaluate your model in the Firebase console
                // to determine an appropriate value.
                .build()
        val labeler = FirebaseVision.getInstance().getOnDeviceAutoMLImageLabeler(options)
        val image = FirebaseVisionImage.fromBitmap(detectedObject.getBitmap())

        labeler.processImage(image)
                .addOnSuccessListener { labels ->
                    var productTest: Any? = null
                    if (labels.size > 0) {
                        for (label in labels) {
                            val productLabel = label.text
                            //SELECT type

                            insertScanHistory(productLabel)


                            val urlCheckTpye = IPAddress.ipAddress+"product-data/selectProductType/${productLabel}"
                            var type:String? = null

                            val listenerCheckTpye = object : SelectProductType.getDataComplete{
                                override fun getDataComplete(productList: List<Product>) {

                                    val product = productList[0]

                                    val type = product.category!!.categoryName

                                    if (type == "Food") {
                                        val urlSelectData = IPAddress.ipAddress+"product-data/selectProductFoodData/${productLabel}"
                                        val listenerSelectData = object : SelectProductFoodData.getDataComplete{
                                            override fun getDataComplete(foodAndBevList: List<FoodAndBev>) {

                                                val foodAndBev = foodAndBevList[0]

                                                listener.invoke(detectedObject, foodAndBev)
                                            }

                                        }

                                        SelectProductFoodData(listenerSelectData).execute(urlSelectData)



                                    } else if (type == "Electronic") {
                                        val urlSelectData = IPAddress.ipAddress+"product-data/selectProductElectronicData/${productLabel}"
                                        val listenerSelectData = object : SelectProductElectronic.getDataComplete{
                                            override fun getDataComplete(electronicList: List<Electronic>) {

                                                val electronic = electronicList[0]

                                                listener.invoke(detectedObject, electronic)
                                            }

                                        }

                                        SelectProductElectronic(listenerSelectData).execute(urlSelectData)

                                    } else if (type == "Furniture") {
                                        val urlSelectData = IPAddress.ipAddress+"product-data/selectProductFurnitureData/${productLabel}"
                                        val listenerSelectData = object : SelectProductFurniture.getDataComplete{
                                            override fun getDataComplete(furnitureList: List<Furniture>) {

                                                val furniture = furnitureList[0]

                                                listener.invoke(detectedObject, furniture)
                                            }

                                        }

                                        SelectProductFurniture(listenerSelectData).execute(urlSelectData)
                                    }
//                                    listener.invoke(detectedObject, productTest)
                                }
                            }

                            SelectProductType(listenerCheckTpye).execute(urlCheckTpye).get()

                        }


                    } else {
//                        productTest = Response_info_data("","","",
//                                "","",
//                                "","",
//                                "","",
//                                "")
                        Log.d("boom", "sadasd")
                        listener.invoke(detectedObject, productTest)
                    }


                }
                .addOnFailureListener { e ->
                    // Task failed with an exception
                    // ...
                }
    }

    private fun insertScanHistory(productId:String) {
        val insertUserDataScan = IPAddress.ipAddress+"user-data/insertUserDataScanHistory/"
        val pref:SharedPreferences? = context.getSharedPreferences("SP_USER_DATA", Context.MODE_PRIVATE)

        var scanHistory = ScanHistory(pref!!.getString("UUID", ""),null,productId,null,null)

        InsertUserDataScanHistory(scanHistory).execute(insertUserDataScan)
    }

//    private fun chkType(productLabel:String): String? {
//        val url = ip+"selectProductType/${productLabel}"
//        var type:String? = null
//
//        val listener = object : AsyncTaskHandleJson.getDataComplete{
//            override fun getDataComplete(jsonString: String) {
//                val jsonArray = JSONArray(jsonString)
//
//                val jsonObject = jsonArray.getJSONObject(0)
//
//                type = jsonObject.getJSONObject("category").getString("categoryName")
//
//
////                selectProductData(productLabel,type)
//
//
//
//            }
//        }
//
//         AsyncTaskHandleJson(listener).execute(url).get()
//
//        return type
//    }


    fun shutdown() {
        searchRequestQueue.cancelAll(TAG)
        requestCreationExecutor.shutdown()
    }

    companion object {
        private const val TAG = "SearchEngine"

        @Throws(Exception::class)
        private fun createRequest(searchingObject: DetectedObject): JsonObjectRequest {
            val objectImageData = searchingObject.imageData
                    ?: throw Exception("Failed to get object image data!")
            //#################################################################################


            //#################################################################################

            // Hooks up with your own product search backend here.
            throw Exception("Hooks up with your own product search backend.")
        }

    }
}

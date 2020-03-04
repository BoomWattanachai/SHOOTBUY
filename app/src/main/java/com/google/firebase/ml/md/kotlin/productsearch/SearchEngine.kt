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

package com.google.firebase.ml.md.kotlin.productsearch

import android.content.Context
import android.util.Log
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.ml.md.kotlin.Models.BeverageCanList
import com.google.firebase.ml.md.kotlin.Models.FurnitureList
import com.google.firebase.ml.md.kotlin.Models.Response.Response_Electronic
import com.google.firebase.ml.md.kotlin.Models.Response.Response_FoodAndBev
import com.google.firebase.ml.md.kotlin.Models.Response.Response_Furniture
import com.google.firebase.ml.md.kotlin.Models.Service.AsyncTaskHandleJson
import com.google.firebase.ml.md.kotlin.objectdetection.DetectedObject
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.automl.FirebaseAutoMLLocalModel
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.label.FirebaseVisionOnDeviceAutoMLImageLabelerOptions
import org.json.JSONArray
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/** A fake search engine to help simulate the complete work flow.  */
class SearchEngine(context: Context) {

    private val searchRequestQueue: RequestQueue = Volley.newRequestQueue(context)
    private val requestCreationExecutor: ExecutorService = Executors.newSingleThreadExecutor()
    private val productInfo = mutableListOf("Kohli", "Smith", "Root")
    private val ip:String = "http://100.66.112.138:3000/product-data/"
    fun search(
            detectedObject: DetectedObject,
//        listener: (detectedObject: DetectedObject, productList: List<Product>) -> Unit
            listener: (detectedObject: DetectedObject, productTest: Any?) -> Unit
    ) {
//        // Crops the object image out of the full image is expensive, so do it off the UI thread.
//        Tasks.call<JsonObjectRequest>(requestCreationExecutor, Callable { createRequest(detectedObject) })
//                .addOnSuccessListener {
//                    productRequest -> searchRequestQueue.add(productRequest.setTag(TAG))
//                }
//                .addOnFailureListener { e ->
//                    Log.e(TAG, "Failed to create product search request!", e)
//                    // Remove the below dummy code after your own product search backed hooked up.
//                    val productList = ArrayList<Product>()
////                    for (i in 0..1) {
//                        productList.add(
////                                Product(/* imageUrl= */"", "Product title $i", "Product subtitle $i"))
//                                Product(/* imageUrl= */"", "Product title ", "Product subtitle"))
////                    }
//                    listener.invoke(detectedObject, productList)
//                }

        val localModel = FirebaseAutoMLLocalModel.Builder()
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




                            val urlCheckTpye = ip+"selectProductType/${productLabel}"
                            var type:String? = null

                            val listenerCheckTpye = object : AsyncTaskHandleJson.getDataComplete{
                                override fun getDataComplete(jsonString: String) {
                                    var jsonArray = JSONArray(jsonString)

                                    var jsonObject = jsonArray.getJSONObject(0)

                                    type = jsonObject.getJSONObject("category").getString("categoryName")



                                    if (type == "Food") {
                                        val urlSelectData = ip+"selectProductFoodData/${productLabel}"
                                        val listenerSelectData = object : AsyncTaskHandleJson.getDataComplete{
                                            override fun getDataComplete(jsonString: String) {
                                                jsonArray = JSONArray(jsonString)

                                                jsonObject = jsonArray.getJSONObject(0)
//                                                val subJsonObject = jsonObject.getJSONObject("foodAndBev")

                                                productTest = Response_FoodAndBev(jsonObject.getString("foodAndBevId"),jsonObject.getString("foodAndBevBrand")
                                                        ,jsonObject.getString("foodAndBevModel"),jsonObject.getString("foodAndBevImage"),jsonObject.getString("foodAndBevSize")
                                                        ,jsonObject.getInt("foodAndBevPrice"),jsonObject.getString("foodAndBevCal"),jsonObject.getString("foodAndBevSugar")
                                                        ,jsonObject.getString("foodAndBevFat"),jsonObject.getString("foodAndBevSodium"),jsonObject.getInt("foodAndBevAmount"))

                                                listener.invoke(detectedObject, productTest)
                                            }

                                        }

                                        AsyncTaskHandleJson(listenerSelectData).execute(urlSelectData)



                                    } else if (type == "Electronic") {
                                        val urlSelectData = ip+"selectProductElectronicData/${productLabel}"
                                        val listenerSelectData = object : AsyncTaskHandleJson.getDataComplete{
                                            override fun getDataComplete(jsonString: String) {
                                                jsonArray = JSONArray(jsonString)

                                                jsonObject = jsonArray.getJSONObject(0)
//                                                val subJsonObject = jsonObject.getJSONObject("Electronic")

                                                productTest = Response_Electronic(jsonObject.getString("electronicId"),jsonObject.getString("electronicBrand")
                                                        ,jsonObject.getString("electronicModel"),jsonObject.getString("electronicImage"),jsonObject.getInt("electronicPrice")
                                                        ,jsonObject.getString("electronicSpec"),jsonObject.getInt("electronicAmount"))

                                                listener.invoke(detectedObject, productTest)
                                            }

                                        }

                                        AsyncTaskHandleJson(listenerSelectData).execute(urlSelectData)

                                    } else if (type == "Furniture") {
                                        val urlSelectData = ip+"selectProductFurnitureData/${productLabel}"
                                        val listenerSelectData = object : AsyncTaskHandleJson.getDataComplete{
                                            override fun getDataComplete(jsonString: String) {
                                                jsonArray = JSONArray(jsonString)

                                                jsonObject = jsonArray.getJSONObject(0)
//                                                val subJsonObject = jsonObject.getJSONObject("Furniture")

                                                productTest = Response_Furniture(jsonObject.getString("furnitureId"),jsonObject.getString("furnitureBrand")
                                                        ,jsonObject.getString("furnitureModel"),jsonObject.getString("furnitureImage"),jsonObject.getInt("furniturePrice")
                                                        ,jsonObject.getString("furnitureSize"),jsonObject.getString("furnitureDetail"),jsonObject.getInt("furnitureAmount"))

                                                listener.invoke(detectedObject, productTest)
                                            }

                                        }

                                        AsyncTaskHandleJson(listenerSelectData).execute(urlSelectData)
                                    }
//                                    listener.invoke(detectedObject, productTest)
                                }
                            }

                            AsyncTaskHandleJson(listenerCheckTpye).execute(urlCheckTpye).get()

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

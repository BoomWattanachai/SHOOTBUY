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
import android.widget.Toast
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.ml.md.kotlin.Models.BeverageCan
import com.google.firebase.ml.md.kotlin.Models.BeverageCanList
import com.google.firebase.ml.md.kotlin.Models.FurnitureList
import com.google.firebase.ml.md.kotlin.Models.Response.Response_info_data
import com.google.firebase.ml.md.kotlin.Models.Response.Response_info_data2
import com.google.firebase.ml.md.kotlin.objectdetection.DetectedObject
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.automl.FirebaseAutoMLLocalModel
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.label.FirebaseVisionOnDeviceAutoMLImageLabelerOptions
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.annotation.Nullable

/** A fake search engine to help simulate the complete work flow.  */
class SearchEngine(context: Context) {

    private val searchRequestQueue: RequestQueue = Volley.newRequestQueue(context)
    private val requestCreationExecutor: ExecutorService = Executors.newSingleThreadExecutor()
    private val productInfo = mutableListOf("Kohli", "Smith", "Root")
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
                .setAssetFilePath("Models/manifest.json")
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
                            val text = label.text
                            var type: String? = null
                            if (text == "Coke" || text=="Pepsi" || text=="Calpis"){
                                type = "BeverageCan"
                                for(i in BeverageCanList.beverageCanList!!){
                                    if(text == i.nameData) productTest = i
                                }
                            }
                            else
                            {
                                type = "Furniture"
                                for(i in FurnitureList.furnitureList!!){
                                    if(text == i.nameData) productTest = i
                                }
                            }
//                            else if(text == "Gaming_chair" || text=="Office_chair"||text=="Wicker_chair"||text=="Wood_chair"){
//                                type = "Furniture"
//                            }
//                            else if(text == "Cooling_fan" || text=="Monitor"){
//                                type = "Electronics"
//                            }
//                            else if(text == "Notebook"){
//                                type = "Electronics"
//                            }
//                        val confidence = label.confidence
//                                when (type) {
//                                    "Coke" -> productTest = Response_info_data(text, "325 ml.", "0 Kcal.",
//                                            "0g 0%", "0g",
//                                            "0g 0%", "0g",
//                                            "25mg 1%", "สีธรรมชาติ (INS150d) สารควบคุมการเป็นกรด (INS338,INS 331(iii)) สารให้ความหวาน (แอซีซัลเฟมโพแทสเซียมและซูคราโลส) แต่งกลิ่นธรรมชาติ วัตถุกันเสีย (INS 211) ไม่ใช่อาหารสำหรับควบคุมน้ำหนัก",
//                                            "15")
//                                    "Pepsi" -> productTest = Response_info_data(text, "245 ml.", "0 Kcal.",
//                                            "0g 0%", "0g",
//                                            "0g 0%", "0g",
//                                            "0g 0%", "แต่งกลิ่นธรรมชาติ (สารควบคุมความเป็นกรด (INS330,INS331(iii),INS338) สีสังเคราะห์ (INS 150d) วัตถุกันเสีย (INS211) วัตถุให้ความหวานแทนน้ำตาล(แอสปาแตม,อะซีซัลเฟม โพแทสเซียม)",
//                                            "12")
//                                    "Calpis" -> productTest = Response_info_data2(text, "60บาท")
//                                }
                        }
                        listener.invoke(detectedObject, productTest)

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

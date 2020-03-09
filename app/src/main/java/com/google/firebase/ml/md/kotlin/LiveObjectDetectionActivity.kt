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

package com.google.firebase.ml.md.kotlin

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.hardware.Camera
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.chip.Chip
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.common.base.Objects
import com.google.firebase.ml.md.R
import com.google.firebase.ml.md.kotlin.Cart.CartActivity
import com.google.firebase.ml.md.kotlin.EntityModels.ProductData.Electronic
import com.google.firebase.ml.md.kotlin.EntityModels.ProductData.FoodAndBev
import com.google.firebase.ml.md.kotlin.EntityModels.ProductData.Furniture
import com.google.firebase.ml.md.kotlin.EntityModels.ProductOrder.Order
import com.google.firebase.ml.md.kotlin.EntityModels.ProductOrder.OrderDetail
import com.google.firebase.ml.md.kotlin.HistoryOrder.HistoryOrderActivity
import com.google.firebase.ml.md.kotlin.HistoryScan.HistoryScanActivity
import com.google.firebase.ml.md.kotlin.Models.Service.ProductOrder.InsertProductOrder
//import com.google.firebase.ml.md.kotlin.Models.Service.AsyncTaskPostOrder
import com.google.firebase.ml.md.kotlin.camera.CameraSource
import com.google.firebase.ml.md.kotlin.camera.CameraSourcePreview
import com.google.firebase.ml.md.kotlin.camera.GraphicOverlay
import com.google.firebase.ml.md.kotlin.camera.WorkflowModel
import com.google.firebase.ml.md.kotlin.camera.WorkflowModel.WorkflowState
import com.google.firebase.ml.md.kotlin.objectdetection.MultiObjectProcessor
import com.google.firebase.ml.md.kotlin.objectdetection.ProminentObjectProcessor
import com.google.firebase.ml.md.kotlin.productSearch.BottomSheetScrimView
import com.google.firebase.ml.md.kotlin.productSearch.SearchEngine
import com.google.firebase.ml.md.kotlin.settings.PreferenceUtils
import com.google.firebase.ml.md.kotlin.settings.SettingsActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.beverage_layout.view.*
import kotlinx.android.synthetic.main.electronic_layout.view.*
import kotlinx.android.synthetic.main.furniture_layout.view.*
import java.io.IOException
import java.text.NumberFormat

/** Demonstrates the object detection and visual search workflow using camera preview.  */
class LiveObjectDetectionActivity : AppCompatActivity(), OnClickListener {

    private var cameraSource: CameraSource? = null
    private var preview: CameraSourcePreview? = null
    private var graphicOverlay: GraphicOverlay? = null
    private var settingsButton: View? = null
    private var flashButton: View? = null
    private var promptChip: Chip? = null
    private var promptChipAnimator: AnimatorSet? = null
    private var searchButton: ExtendedFloatingActionButton? = null
    private var searchButtonAnimator: AnimatorSet? = null
    private var searchProgressBar: ProgressBar? = null
    private var workflowModel: WorkflowModel? = null
    private var currentWorkflowState: WorkflowState? = null
    private var searchEngine: SearchEngine? = null

    private var bottomSheetBehavior: BottomSheetBehavior<View>? = null
    private var bottomSheetScrimView: BottomSheetScrimView? = null
    private var productRecyclerView: RecyclerView? = null
    private var bottomSheetTitleView: TextView? = null
    private var objectThumbnailForBottomSheet: Bitmap? = null
    private var slidingSheetUpFromHiddenState: Boolean = false
    private var mainCustomLayout: LinearLayout? = null
    private var cartBtn: ImageButton? = null
    private var historyScanBtn: Button? = null
    private var historyOrderBtn: Button? = null
    private var addCart: Button? = null
    private var removeCart: Button? = null
    var testUI: TextView? = null
    var testUI1: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val pref = getSharedPreferences("SP_USER_DATA", Context.MODE_PRIVATE)

        val editor = pref.edit()

        editor.putString("UUID", "t1yRb9sauWQxmLy6E8kc02vm7QA3")
        editor.commit()

        searchEngine = SearchEngine(applicationContext)

        setContentView(R.layout.activity_live_object_kotlin)
        preview = findViewById(R.id.camera_preview)

        testUI = findViewById(R.id.card_title)
        testUI1 = findViewById(R.id.card_subtitle)

        mainCustomLayout = findViewById(R.id.mainCustomLayout)
        graphicOverlay = findViewById<GraphicOverlay>(R.id.camera_preview_graphic_overlay).apply {
            setOnClickListener(this@LiveObjectDetectionActivity)
            cameraSource = CameraSource(this)
        }
        promptChip = findViewById(R.id.bottom_prompt_chip)
        promptChipAnimator =
                (AnimatorInflater.loadAnimator(this, R.animator.bottom_prompt_chip_enter) as AnimatorSet).apply {
                    setTarget(promptChip)
                }
        searchButton = findViewById<ExtendedFloatingActionButton>(R.id.product_search_button).apply {
            setOnClickListener(this@LiveObjectDetectionActivity)
        }
        searchButtonAnimator =
                (AnimatorInflater.loadAnimator(this, R.animator.search_button_enter) as AnimatorSet).apply {
                    setTarget(searchButton)
                }
        searchProgressBar = findViewById(R.id.search_progress_bar)
        setUpBottomSheet()

        findViewById<View>(R.id.close_button).setOnClickListener(this)
        flashButton = findViewById<View>(R.id.flash_button).apply {
            setOnClickListener(this@LiveObjectDetectionActivity)
        }
        settingsButton = findViewById<View>(R.id.settings_button).apply {
            setOnClickListener(this@LiveObjectDetectionActivity)
        }
        cartBtn = findViewById<ImageButton>(R.id.cartBtn).apply {
            setOnClickListener(this@LiveObjectDetectionActivity)
        }

        historyScanBtn = findViewById<Button>(R.id.historyScanBtn).apply {
            setOnClickListener(this@LiveObjectDetectionActivity)
        }

        historyOrderBtn = findViewById<Button>(R.id.historyOrderBtn).apply {
            setOnClickListener(this@LiveObjectDetectionActivity)
        }

        setUpWorkflowModel()
    }

    override fun onResume() {
        super.onResume()

        if (!Utils.allPermissionsGranted(this)) {
            Utils.requestRuntimePermissions(this)
        }

        workflowModel?.markCameraFrozen()
        settingsButton?.isEnabled = true
        bottomSheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
        currentWorkflowState = WorkflowState.NOT_STARTED
        cameraSource?.setFrameProcessor(
                if (PreferenceUtils.isMultipleObjectsMode(this)) {
                    MultiObjectProcessor(graphicOverlay!!, workflowModel!!)
                } else {
                    ProminentObjectProcessor(graphicOverlay!!, workflowModel!!)
                }
        )
        workflowModel?.setWorkflowState(WorkflowState.DETECTING)
    }

    override fun onPause() {
        super.onPause()
        currentWorkflowState = WorkflowState.NOT_STARTED
        stopCameraPreview()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraSource?.release()
        cameraSource = null
        searchEngine?.shutdown()
    }

    override fun onBackPressed() {
        if (bottomSheetBehavior?.state != BottomSheetBehavior.STATE_HIDDEN) {
            bottomSheetBehavior?.setState(BottomSheetBehavior.STATE_HIDDEN)
        } else {
            super.onBackPressed()
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.product_search_button -> {
                searchButton?.isEnabled = false
                workflowModel?.onSearchButtonClicked()
            }
//            R.id.bottom_sheet_scrim_view -> bottomSheetBehavior?.setState(BottomSheetBehavior.STATE_HIDDEN)
            R.id.close_button -> onBackPressed()
            R.id.flash_button -> {
                if (flashButton?.isSelected == true) {
                    flashButton?.isSelected = false
                    cameraSource?.updateFlashMode(Camera.Parameters.FLASH_MODE_OFF)
                } else {
                    flashButton?.isSelected = true
                    cameraSource?.updateFlashMode(Camera.Parameters.FLASH_MODE_TORCH)
                }
            }
            R.id.settings_button -> {
                settingsButton?.isEnabled = false
                startActivity(Intent(this, SettingsActivity::class.java))
            }
            R.id.cartBtn -> {
//                if (Cart.cartItemList.size > 0)
                startActivity(Intent(this, CartActivity::class.java))
//                else
//                    Toast.makeText(this, "Cart is empty!", Toast.LENGTH_SHORT).show()
            }

            R.id.historyScanBtn -> {
                startActivity(Intent(this, HistoryScanActivity::class.java))
            }

            R.id.historyOrderBtn -> {
                startActivity(Intent(this, HistoryOrderActivity::class.java))
            }

        }
    }

    private fun startCameraPreview() {
        val cameraSource = this.cameraSource ?: return
        val workflowModel = this.workflowModel ?: return
        if (!workflowModel.isCameraLive) {
            try {
                workflowModel.markCameraLive()
                preview?.start(cameraSource)
            } catch (e: IOException) {
                Log.e(TAG, "Failed to start camera preview!", e)
                cameraSource.release()
                this.cameraSource = null
            }
        }
    }

    private fun stopCameraPreview() {
        if (workflowModel?.isCameraLive == true) {
            workflowModel!!.markCameraFrozen()
            flashButton?.isSelected = false
            preview?.stop()
        }
    }

    private fun setUpBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.bottom_sheet))
        bottomSheetBehavior?.setBottomSheetCallback(
                object : BottomSheetBehavior.BottomSheetCallback() {
                    override fun onStateChanged(bottomSheet: View, newState: Int) {
                        Log.d(TAG, "Bottom sheet new state: $newState")
                        bottomSheetScrimView?.visibility =
                                if (newState == BottomSheetBehavior.STATE_HIDDEN) View.GONE else View.VISIBLE
                        graphicOverlay?.clear()

                        when (newState) {
                            BottomSheetBehavior.STATE_HIDDEN -> workflowModel?.setWorkflowState(WorkflowState.DETECTING)
                            BottomSheetBehavior.STATE_COLLAPSED,
                            BottomSheetBehavior.STATE_EXPANDED,
                            BottomSheetBehavior.STATE_HALF_EXPANDED -> slidingSheetUpFromHiddenState = false
                            BottomSheetBehavior.STATE_DRAGGING, BottomSheetBehavior.STATE_SETTLING -> {
                            }
                        }
                    }

                    override fun onSlide(bottomSheet: View, slideOffset: Float) {
                        val searchedObject = workflowModel!!.searchedObject.value
                        if (searchedObject == null || java.lang.Float.isNaN(slideOffset)) {
                            return
                        }

                        val graphicOverlay = graphicOverlay ?: return
                        val bottomSheetBehavior = bottomSheetBehavior ?: return
                        val collapsedStateHeight = bottomSheetBehavior.peekHeight.coerceAtMost(bottomSheet.height)
                        val bottomBitmap = objectThumbnailForBottomSheet ?: return
                        if (slidingSheetUpFromHiddenState) {
                            val thumbnailSrcRect = graphicOverlay.translateRect(searchedObject.boundingBox)
                            bottomSheetScrimView?.updateWithThumbnailTranslateAndScale(
                                    bottomBitmap,
                                    collapsedStateHeight,
                                    slideOffset,
                                    thumbnailSrcRect)
                        } else {
                            bottomSheetScrimView?.updateWithThumbnailTranslate(
                                    bottomBitmap, collapsedStateHeight, slideOffset, bottomSheet)
                        }
                    }
                })

        bottomSheetScrimView = findViewById<BottomSheetScrimView>(R.id.bottom_sheet_scrim_view).apply {
            setOnClickListener(this@LiveObjectDetectionActivity)
        }

        bottomSheetTitleView = findViewById(R.id.bottom_sheet_title)
//        productRecyclerView = findViewById<RecyclerView>(R.id.product_recycler_view).apply {
//            setHasFixedSize(true)
//            layoutManager = LinearLayoutManager(this@LiveObjectDetectionActivity)
//            adapter = ProductAdapter(ImmutableList.of())
//        }
    }

    private fun setUpWorkflowModel() {
        workflowModel = ViewModelProviders.of(this).get(WorkflowModel::class.java).apply {

            // Observes the workflow state changes, if happens, update the overlay view indicators and
            // camera preview state.
            workflowState.observe(this@LiveObjectDetectionActivity, Observer { workflowState ->
                if (workflowState == null || Objects.equal(currentWorkflowState, workflowState)) {
                    return@Observer
                }
                currentWorkflowState = workflowState
                Log.d(TAG, "Current workflow state: ${workflowState.name}")

                if (PreferenceUtils.isAutoSearchEnabled(this@LiveObjectDetectionActivity)) {
                    stateChangeInAutoSearchMode(workflowState)
                } else {
                    stateChangeInManualSearchMode(workflowState)
                }
            })

            // Observes changes on the object to search, if happens, fire product search request.
            objectToSearch.observe(this@LiveObjectDetectionActivity, Observer { detectObject ->
                searchEngine!!.search(detectObject) { detectedObject, productTest ->
                    workflowModel?.onSearchCompleted(detectedObject, productTest)
                }
            })

            // Observes changes on the object that has search completed, if happens, show the bottom sheet
            // to present search result.
            searchedObject.observe(this@LiveObjectDetectionActivity, Observer { nullableSearchedObject ->
                val searchedObject = nullableSearchedObject ?: return@Observer
                val productList = searchedObject.productList

                objectThumbnailForBottomSheet = searchedObject.getObjectThumbnail()
//                bottomSheetTitleView?.text = resources
//                        .getQuantityString(
//                                R.plurals.bottom_sheet_title, productList.size, productList.size)
//                bottomSheetTitleView?.text = "Boom Test Text"

//                productRecyclerView?.adapter = ProductAdapter(productList)

//                card_title.text = searchedObject.productList.text1
//                card_subtitle.text = searchedObject.productList.text2
//                  testUI!!.text = searchedObject.productList.text1
//                  testUI1?.text = searchedObject.productList.text2


                if (searchedObject.productList != null) {
                    setData(searchedObject.productList)

                    slidingSheetUpFromHiddenState = true
                    bottomSheetBehavior?.peekHeight =
                            preview?.height?.div(1) ?: BottomSheetBehavior.PEEK_HEIGHT_AUTO
                    bottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
//                    bottomSheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED

                } else {
                    Toast.makeText(this@LiveObjectDetectionActivity, "Not Found!!", Toast.LENGTH_SHORT).show()
                    workflowModel?.setWorkflowState(WorkflowState.DETECTING)
                }


            })
        }
    }

    private fun setData(productData: Any?) {
        val pref = getSharedPreferences("SP_USER_DATA", Context.MODE_PRIVATE)
        mainCustomLayout?.removeAllViews()
        if (productData is FoodAndBev) {
            var wizardView = layoutInflater.inflate(R.layout.beverage_layout, mainCustomLayout, false)
            mainCustomLayout?.addView(wizardView)

            var imageView: ImageView = findViewById(R.id.food_and_bev_image)
            Picasso.get().load(productData.foodAndBevImage).into(imageView)
//            wizardView.product_image.setImageResource(productData.imageResource)
//            wizardView.food_and_bev_image.setImageResource(R.drawable.coke_no_sugar)
            wizardView.food_and_bev_brand.text = productData.foodAndBevBrand
            wizardView.food_and_bev_vol.text = "(" + productData.foodAndBevSize + ")"
            wizardView.food_and_bev_price.text = "$" + NumberFormat.getInstance().format(productData.foodAndBevPrice).toString()
            wizardView.food_and_bev_cal.text = productData.foodAndBevCal
            wizardView.food_and_bev_sugar.text = productData.foodAndBevSugar
            wizardView.food_and_bev_fat.text = productData.foodAndBevFat
            wizardView.food_and_bev_sodium.text = productData.foodAndBevSodium

            var amount: Int = 1
            wizardView.food_and_bev_beverage_increase.setOnClickListener {
                amount++
                wizardView.amount.text = amount.toString()
            }
            wizardView.food_and_bev_beverage_decrease.setOnClickListener {
                if (amount > 1) amount--
                wizardView.amount.text = amount.toString()
            }
            wizardView.food_and_bev_close.setOnClickListener {
                bottomSheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
            }

            wizardView.food_and_bev_add_to_cart.setOnClickListener {
                var order = Order(null, pref.getString("UUID", ""), null, null,
                        null, null, arrayOf(OrderDetail(null, null, productData.foodAndBevId,
                        null, amount)).toList())

                val listenerPostOrder = object : InsertProductOrder.getDataComplete {
                    override fun getDataComplete(jsonString: String) {
                        bottomSheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
                    }
                }

                InsertProductOrder(listenerPostOrder, order).execute(IPAddress.ipAddress + "product-order/insertProductOrder/")
            }


        } else if (productData is Furniture) {
            var wizardView = layoutInflater.inflate(R.layout.furniture_layout, mainCustomLayout, false)
            mainCustomLayout?.addView(wizardView)

            var imageView: ImageView = findViewById(R.id.furniture_image)
            Picasso.get().load(productData.furnitureImage).into(imageView)
//            wizardView.furniture_product_image.setImageResource(productData.imageResource)
            wizardView.furniture_image.setImageResource(R.drawable.gaming_chair)
            wizardView.furniture_brand.text = productData.furnitureBrand
            wizardView.furniture_model.text = productData.furnitureModel
            wizardView.furniture_spec.text = productData.furnitureDetail
            wizardView.furniture_price.text = "$" + NumberFormat.getInstance().format(productData.furniturePrice).toString()
            var amount: Int = 1
            wizardView.furniture_increase.setOnClickListener {
                amount++
                wizardView.furniture_amount.text = amount.toString()
            }
            wizardView.furniture_decrease.setOnClickListener {
                if (amount > 1) amount--
                wizardView.furniture_amount.text = amount.toString()
            }
            wizardView.furniture_close.setOnClickListener {
                bottomSheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
            }
            wizardView.furniture_add_to_cart.setOnClickListener {

                var order = Order(null, pref.getString("UUID", ""), null, null,
                        null, null, arrayOf(OrderDetail(null, null, productData.furnitureId,
                        null, amount)).toList())

                val listenerPostOrder = object : InsertProductOrder.getDataComplete {
                    override fun getDataComplete(jsonString: String) {
                        bottomSheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
                    }
                }

                InsertProductOrder(listenerPostOrder, order).execute(IPAddress.ipAddress + "product-order/insertProductOrder/")
            }

        }
        else if (productData is Electronic) {
            var wizardView = layoutInflater.inflate(R.layout.electronic_layout, mainCustomLayout, false)
            mainCustomLayout?.addView(wizardView)

            var imageView: ImageView = findViewById(R.id.electronic_image)
            Picasso.get().load(productData.electronicImage).into(imageView)
//            wizardView.furniture_product_image.setImageResource(productData.imageResource)
            wizardView.electronic_brand.text = productData.electronicBrand
            wizardView.electronic_model.text = productData.electronicModel
            wizardView.electronic_spec.text = productData.electronicSpec
            wizardView.electronic_price.text = "$" + NumberFormat.getInstance().format(productData.electronicPrice).toString()
            var amount: Int = 1
            wizardView.electronic_increase.setOnClickListener {
                amount++
                wizardView.electronic_amount.text = amount.toString()
            }
            wizardView.electronic_decrease.setOnClickListener {
                if (amount > 1) amount--
                wizardView.electronic_amount.text = amount.toString()
            }
            wizardView.electronic_close.setOnClickListener {
                bottomSheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
            }
            wizardView.electronic_add_to_cart.setOnClickListener {

                var order = Order(null, pref.getString("UUID", ""), null, null,
                        null, null, arrayOf(OrderDetail(null, null, productData.electronicId,
                        null, amount)).toList())

                val listenerPostOrder = object : InsertProductOrder.getDataComplete {
                    override fun getDataComplete(jsonString: String) {
                        bottomSheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
                    }
                }

                InsertProductOrder(listenerPostOrder, order).execute(IPAddress.ipAddress + "product-order/insertProductOrder/")

            }

        }

    }

    private fun stateChangeInAutoSearchMode(workflowState: WorkflowState) {
        val wasPromptChipGone = promptChip!!.visibility == View.GONE

        searchButton?.visibility = View.GONE
        searchProgressBar?.visibility = View.GONE
        when (workflowState) {
            WorkflowState.DETECTING, WorkflowState.DETECTED, WorkflowState.CONFIRMING -> {
                promptChip?.visibility = View.VISIBLE
                promptChip?.setText(
                        if (workflowState == WorkflowState.CONFIRMING)
                            R.string.prompt_hold_camera_steady
                        else
                            R.string.prompt_point_at_an_object)
                startCameraPreview()
            }
            WorkflowState.CONFIRMED -> {
                promptChip?.visibility = View.VISIBLE
                promptChip?.setText(R.string.prompt_searching)
                stopCameraPreview()
            }
            WorkflowState.SEARCHING -> {
                searchProgressBar?.visibility = View.VISIBLE
                promptChip?.visibility = View.VISIBLE
                promptChip?.setText(R.string.prompt_searching)
                stopCameraPreview()
            }
            WorkflowState.SEARCHED -> {
                promptChip?.visibility = View.GONE
                stopCameraPreview()
            }
            else -> promptChip?.visibility = View.GONE
        }

        val shouldPlayPromptChipEnteringAnimation = wasPromptChipGone && promptChip?.visibility == View.VISIBLE
        if (shouldPlayPromptChipEnteringAnimation && promptChipAnimator?.isRunning == false) {
            promptChipAnimator?.start()
        }
    }

    private fun stateChangeInManualSearchMode(workflowState: WorkflowState) {
        val wasPromptChipGone = promptChip?.visibility == View.GONE
        val wasSearchButtonGone = searchButton?.visibility == View.GONE

        searchProgressBar?.visibility = View.GONE
        when (workflowState) {
            WorkflowState.DETECTING, WorkflowState.DETECTED, WorkflowState.CONFIRMING -> {
                promptChip?.visibility = View.VISIBLE
                promptChip?.setText(R.string.prompt_point_at_an_object)
                searchButton?.visibility = View.GONE
                startCameraPreview()
            }
            WorkflowState.CONFIRMED -> {
                promptChip?.visibility = View.GONE
                searchButton?.visibility = View.VISIBLE
                searchButton?.isEnabled = true
                searchButton?.setBackgroundColor(Color.WHITE)
                startCameraPreview()
            }
            WorkflowState.SEARCHING -> {
                promptChip?.visibility = View.GONE
                searchButton?.visibility = View.VISIBLE
                searchButton?.isEnabled = false
                searchButton?.setBackgroundColor(Color.GRAY)
                searchProgressBar!!.visibility = View.VISIBLE
                stopCameraPreview()
            }
            WorkflowState.SEARCHED -> {
                promptChip?.visibility = View.GONE
                searchButton?.visibility = View.GONE
                stopCameraPreview()
            }
            else -> {
                promptChip?.visibility = View.GONE
                searchButton?.visibility = View.GONE
            }
        }

        val shouldPlayPromptChipEnteringAnimation = wasPromptChipGone && promptChip?.visibility == View.VISIBLE
        promptChipAnimator?.let {
            if (shouldPlayPromptChipEnteringAnimation && !it.isRunning) it.start()
        }

        val shouldPlaySearchButtonEnteringAnimation = wasSearchButtonGone && searchButton?.visibility == View.VISIBLE
        searchButtonAnimator?.let {
            if (shouldPlaySearchButtonEnteringAnimation && !it.isRunning) it.start()
        }
    }

    companion object {
        private const val TAG = "LiveObjectActivity"
    }
}

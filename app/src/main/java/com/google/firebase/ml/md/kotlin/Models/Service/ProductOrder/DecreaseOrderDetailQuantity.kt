package com.google.firebase.ml.md.kotlin.Models.Service.ProductOrder

import android.os.AsyncTask
import android.util.Log
import com.google.firebase.ml.md.kotlin.EntityModels.ProductOrder.Order
import com.google.firebase.ml.md.kotlin.EntityModels.ProductOrder.OrderDetail
import com.google.gson.Gson
import java.net.HttpURLConnection
import java.net.URL

class DecreaseOrderDetailQuantity (var data: OrderDetail): AsyncTask<String, String, String>() {
    //    var listener:getDataComplete? = null
    override fun doInBackground(vararg url: String?): String {


        var text: String
        val connection = URL(url[0]).openConnection() as HttpURLConnection
//        connection.responseCode
//        Log.d("Con1",connection.responseCode.toString())

        try {
            var gson = Gson()
            var jsonString = gson.toJson(data)
            connection.doOutput = true
            connection.setRequestProperty("Content-Type", "application/json")
//            connection.responseCode
//            Log.d("Con1",connection.responseCode.toString())
            var outputStream = connection.outputStream

            outputStream.write(jsonString.toByteArray())
            outputStream.flush()
            outputStream.close()
            text = connection.inputStream.use { it.reader().use { reader -> reader.readText() } }
        } finally {
            connection.disconnect()
        }
//        connection.responseCode
        Log.d("responseCode",connection.responseCode.toString())
        return text
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
//        listener.getDataComplete(result!!)
        //handelJson(result)
    }

    interface getDataComplete {
        fun getDataComplete(jsonString: String)
    }
}
package com.google.firebase.ml.md.kotlin.Models.Service

import android.os.AsyncTask
import android.util.Log
import com.google.firebase.ml.md.kotlin.Models.Request.Request_AddCart
import com.google.firebase.ml.md.kotlin.Models.Response.Response_Order
import com.google.gson.Gson
import java.net.HttpURLConnection
import java.net.URL

class AsyncTaskGetCart(var listener:getDataComplete): AsyncTask<String, Void, String>() {
    //    var listener:getDataComplete? = null
    override fun doInBackground(vararg url: String?): String {


        var text: String
        val connection = URL(url[0]).openConnection() as HttpURLConnection
//        connection.responseCode
//        Log.d("Con1",connection.responseCode.toString())
        connection.connect()
        try {

            text = connection.inputStream.use { it.reader().use { reader -> reader.readText() } }
        } finally {
            connection.disconnect()
        }
//        connection.responseCode
//        Log.d("responseCode",connection.responseCode.toString())
        return text
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        var gson = Gson()
        var response_order = gson.fromJson(result.toString(),Array<Response_Order>::class.java).toList()
        listener.getDataComplete(response_order!!)
        //handelJson(result)
    }

    interface getDataComplete {
        fun getDataComplete(jsonString: List<Response_Order>)
    }
}
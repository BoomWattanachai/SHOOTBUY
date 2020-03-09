package com.google.firebase.ml.md.kotlin.Models.Service.ProductData

import android.os.AsyncTask
import com.google.firebase.ml.md.kotlin.EntityModels.ProductData.Product
import com.google.gson.Gson
import java.net.HttpURLConnection
import java.net.URL

class SelectProductType(var listener: getDataComplete): AsyncTask<String, String, String>() {
    //    var listener:getDataComplete? = null
    override fun doInBackground(vararg url: String?): String {


        var text: String
        val connection = URL(url[0]).openConnection() as HttpURLConnection
        connection.connect()
        try {
            text = connection.inputStream.use { it.reader().use { reader -> reader.readText() } }
        } finally {
            connection.disconnect()
        }

        return text
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        var gson = Gson()
        var typeProduct = gson.fromJson(result.toString(),Array<Product>::class.java).toList()
        listener.getDataComplete(typeProduct!!)
    }

    interface getDataComplete {
        fun getDataComplete(jsonString: List<Product>)
    }
}
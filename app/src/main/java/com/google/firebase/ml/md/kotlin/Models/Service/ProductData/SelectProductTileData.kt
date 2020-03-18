package com.google.firebase.ml.md.kotlin.Models.Service.ProductData

import android.os.AsyncTask
import com.google.firebase.ml.md.kotlin.EntityModels.ProductData.FoodAndBev
import com.google.firebase.ml.md.kotlin.EntityModels.ProductData.Tile
import com.google.gson.Gson
import java.net.HttpURLConnection
import java.net.URL

class SelectProductTileData (var listener: getDataComplete): AsyncTask<String, String, String>() {
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
        var productTileData = gson.fromJson(result.toString(),Array<Tile>::class.java).toList()
        listener.getDataComplete(productTileData!!)
    }

    interface getDataComplete {
        fun getDataComplete(jsonString: List<Tile>)
    }
}
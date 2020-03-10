package com.google.firebase.ml.md.kotlin.Models.Service.UserData

import android.os.AsyncTask
import com.google.firebase.ml.md.kotlin.EntityModels.UserData.User
import com.google.gson.Gson
import java.net.HttpURLConnection
import java.net.URL

class GetUserDataScanHistory (var listener: getDataComplete): AsyncTask<String, String, String>() {
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
        var userData = gson.fromJson(result.toString(),Array<User>::class.java).toList()
        listener.getDataComplete(userData!!)
    }

    interface getDataComplete {
        fun getDataComplete(jsonString: List<User>)
    }
}
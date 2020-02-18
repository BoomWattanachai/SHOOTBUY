package com.google.firebase.ml.md.kotlin.Models.Service

import android.os.AsyncTask
import java.net.HttpURLConnection
import java.net.URL

class AsyncTaskHandleJson(var listener:getDataComplete): AsyncTask<String, String, String>() {
    //    var listener:getDataComplete? = null
    override fun doInBackground(vararg url: String?): String {

        var text: String
        val connection = URL(url[0]).openConnection() as HttpURLConnection

        try {
            connection.connect()
            text = connection.inputStream.use { it.reader().use { reader -> reader.readText() } }
        } finally {
            connection.disconnect()
        }

        return text

    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        listener.getDataComplete(result!!)
        //handelJson(result)
    }

    interface getDataComplete {
        fun getDataComplete(jsonString: String)
    }
}
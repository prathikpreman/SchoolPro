package com.prathik.schoolpro.httpType

import com.google.gson.Gson
import com.prathik.schoolpro.interfaces.OnHttpResponse
import com.prathik.schoolpro.webapi.coroutine.CoroutineBase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class HttpType(reponseListener: OnHttpResponse) {


    var reponseListener: OnHttpResponse? = reponseListener


    open suspend fun<T> response_GET(httpUrl: String, params:HashMap<String,String>?,outputModelClass: Class<T>) = withContext(Dispatchers.IO) {

        println("||  final res Name54 : http")

        URL(httpUrl).openStream().use { response->

            println("\n||====================|  JSON RESPONSE  |============================================================================================================================================================")
            println("\n||    ")
            println("\n||     HTTP REQUEST TYPE : GET")

            var reqParam = ""
            if (params != null) {
                reqParam = "?"
                for (param in params) {
                    reqParam += "&" + URLEncoder.encode(param.key, "UTF-8") + "=" + URLEncoder.encode(
                        param.value,
                        "UTF-8"
                    )
                }
            }

            println("\n||     HTTP REQUEST URL : ${httpUrl + reqParam}")
            val url = URL(httpUrl + reqParam)

            with(url.openConnection() as HttpURLConnection) {
                requestMethod = "GET"  // optional default is GET

                println("\n||     HTTP RESPONSE CODE : $responseCode")

                var jsonResponse: T? = null

                inputStream.bufferedReader().use {
                    it.lines().forEach { line ->

                        val gson = Gson()

                        jsonResponse = gson.fromJson(line.toString(), outputModelClass)

                        println("\n||     JSON RESPONSE :  $line")
                        println("\n||    ")
                        println("\n||===================================================================================================================================================================================================")

                    }

                    CoroutineBase.instance.launch(Dispatchers.Main) {
                        reponseListener?.onResponse(jsonResponse)
                    }


                }
            }

        }
    }
}
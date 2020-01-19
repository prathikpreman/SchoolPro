package com.prathik.schoolpro.webapi

import android.os.Build
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.internal.Primitives
import com.prathik.schoolpro.interfaces.OnHttpResponse
import com.prathik.schoolpro.webapi.model.UserModel
import kotlinx.coroutines.*
import java.lang.reflect.Type


class WebApi(reponseListener: OnHttpResponse) {




    enum class WebURL(val webUrl:String){
        getChannelInfo("https://reqres.in/api/users")
    }

    var reponseListener:OnHttpResponse? = reponseListener


    open suspend fun<T> getResponse(httpUrl: String, params:HashMap<String,String>?,outputModelClass: Class<T>) = withContext(Dispatchers.IO) {
        URL(httpUrl).openStream().use {response->

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

                        reponseListener?.onResponse(jsonResponse)

                    }
                }

        }
    }



}
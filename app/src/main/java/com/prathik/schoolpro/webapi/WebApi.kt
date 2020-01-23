package com.prathik.schoolpro.webapi

import android.view.View
import com.google.gson.Gson
import com.prathik.schoolpro.httpType.HttpType
import com.prathik.schoolpro.interfaces.OnHttpResponse
import com.prathik.schoolpro.webapi.coroutine.CoroutineBase
import com.prathik.schoolpro.webapi.model.UserModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder


class WebApi(reponseListener: OnHttpResponse) {


    var responseListener: OnHttpResponse? = reponseListener


    enum class WebURL(val webUrl:String){
        GetChannelInfo("https://reqres.in/api/users")
    }




      fun getAllChannels(){

         println("||  final res Name54 : webapi")

        CoroutineBase.instance.launch(Dispatchers.IO) {
            var params:  HashMap<String, String>?
            params= HashMap()
            params["page"] = "2"

                HttpType(object : OnHttpResponse {
                    override fun <T> onResponse(objectResponse: T) {
                        if (objectResponse != null) {

                            responseListener?.onResponse(objectResponse)
                        }
                    }
                }).response_GET(WebURL.GetChannelInfo.webUrl, params, UserModel::class.java)

        }
      }

    fun getAllDelayedChannels(){
        CoroutineBase.instance.launch(Dispatchers.IO) {
            var params:  HashMap<String, String>?
            params= HashMap()
            params["delay"] = "5"

            HttpType(object : OnHttpResponse {
                override fun <T> onResponse(objectResponse: T) {
                    if (objectResponse != null) {

                        responseListener?.onResponse(objectResponse)
                    }
                }
            }).response_GET(WebURL.GetChannelInfo.webUrl, params, UserModel::class.java)

        }
    }








}
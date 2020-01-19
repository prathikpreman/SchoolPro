package com.prathik.schoolpro


import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.coroutines.*
import com.prathik.schoolpro.interfaces.OnHttpResponse
import com.prathik.schoolpro.webapi.WebApi
import com.prathik.schoolpro.webapi.coroutine.CoroutineBase
import com.prathik.schoolpro.webapi.model.UserModel


class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        CoroutineBase. getCoroute().launch(Dispatchers.Main) {
            var params:  HashMap<String, String>?
            params= HashMap()
            params["page"] = "2"

            WebApi(object :OnHttpResponse{
                override fun <T> onResponse(objectResponse: T) {
                    val obj =objectResponse as UserModel
                    println("||   RES : Name: ${obj.data[1].email}")
                    println("||   RES : size: ${obj.data.size}")
                }

            }).getResponse(WebApi.WebURL.getChannelInfo.webUrl,params,UserModel::class.java)

        }

    }


}

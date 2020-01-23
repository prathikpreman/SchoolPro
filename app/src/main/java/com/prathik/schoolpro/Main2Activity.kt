package com.prathik.schoolpro

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.prathik.schoolpro.dataResources.channel.webapi.ChannelDataSource
import com.prathik.schoolpro.interfaces.OnHttpResponse
import com.prathik.schoolpro.webapi.WebApi
import com.prathik.schoolpro.webapi.coroutine.CoroutineBase
import com.prathik.schoolpro.webapi.model.UserModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Main2Activity : AppCompatActivity() {






    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        WebApi(object : OnHttpResponse {
            override fun <T> onResponse(objectResponse: T) {

                val obj =objectResponse as UserModel

                println("||   RESxxxxxxx Delayed : Name: ${obj.data[1].email}")
                println("||   RESxxxxxxx : size: ${obj.data.size}")


            }

        }).getAllDelayedChannels()


         ChannelDataSource(object :OnHttpResponse{
            override fun <T> onResponse(objectResponse: T) {

                val obj =objectResponse as UserModel

                println("||   RESxxxxxxx : Name: ${obj.data[1].email}")
                println("||   RESxxxxxxx : size: ${obj.data.size}")

                progBar.visibility= View.GONE
            }


        }).remote()



    }
}

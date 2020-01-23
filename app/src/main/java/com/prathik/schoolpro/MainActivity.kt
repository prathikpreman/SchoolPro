package com.prathik.schoolpro


import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.coroutines.*
import com.prathik.schoolpro.interfaces.OnHttpResponse
import com.prathik.schoolpro.webapi.WebApi
import com.prathik.schoolpro.webapi.coroutine.CoroutineBase
import com.prathik.schoolpro.webapi.model.UserModel
import com.prathik.schoolpro.dataResources.channel.database.model.ChannelsRealmModel
import io.realm.Realm
import java.util.*
import kotlin.collections.HashMap
import android.util.Log
import android.view.View
import android.widget.Toast
import com.prathik.schoolpro.dataResources.channel.webapi.ChannelDataSource
import com.prathik.schoolpro.util.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity() {


    var channelDataSource:ChannelDataSource?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        println("||  final res Name54 : started")

        Toast.makeText(this@MainActivity,"start",Toast.LENGTH_SHORT).show()

       /* ChannelDataSource().remoteCall(object :OnHttpResponse{
            override fun <T> onResponse(objectResponse: T) {

              CoroutineBase.instance.launch(Dispatchers.Main) {
                    hideLoader()
               }

                val obj =objectResponse as UserModel

                println("||  final res Name54 xxxx : ${obj.data[1].email}")
                println("||  final res Name54 : ${obj.data.size}")

                Toast.makeText(this@MainActivity,"result",Toast.LENGTH_SHORT).show()


               *//*  var usermodalData= ChannelDataSource.GetFromLocal().getAllData()


                if(usermodalData!=null){

                    for (i in 0..usermodalData.size){
                        println("||  final res Name54  Emailxx : ${usermodalData.get(i)?.email}")
                     // usersLoc.text="Email : ${usermodalData[i]?.email} \n -------------------------------"
                    }
                }*//*



            }
        })*/

    }


    fun hideLoader(){
        progBar.visibility= View.GONE
    }


}



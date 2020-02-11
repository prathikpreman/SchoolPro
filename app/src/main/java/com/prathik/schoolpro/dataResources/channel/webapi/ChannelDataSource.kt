package com.prathik.schoolpro.dataResources.channel.webapi

import android.view.View
import com.prathik.schoolpro.dataResources.channel.database.model.ChannelsRealmModel
import com.prathik.schoolpro.interfaces.OnHttpResponse
import com.prathik.schoolpro.webapi.WebApi
import com.prathik.schoolpro.webapi.coroutine.CoroutineBase
import com.prathik.schoolpro.webapi.model.UserModel
import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.delete
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*

 class ChannelDataSource(reponseListener: OnHttpResponse) {

     var thisResponseListener:OnHttpResponse?=null

        init {
             thisResponseListener = reponseListener
        }




           fun remote(){

           WebApi(object : OnHttpResponse {
               override fun <T> onResponse(objectResponse: T) {

                   val obj =objectResponse as UserModel

                   println("||   RESxxxxxxx : Name: ${obj.data[1].email}")
                   println("||   RESxxxxxxx : size: ${obj.data.size}")

                   thisResponseListener?.onResponse(objectResponse)
               }

           }).getAllChannels()

       }



        fun remoteCall(reponseListener: OnHttpResponse){
         println("||  final res Name54 : remoteCall")
            var responseListener: OnHttpResponse? = reponseListener
      //   var responseListener: OnHttpResponse? = reponseListener
          WebApi(object : OnHttpResponse {
              override fun <T> onResponse(objectResponse: T) {
                  if(objectResponse!=null){
                      println("||  final res Name54 : response ok 2:")
                      SaveToLocal(objectResponse)

                      println("||  final res Name54 : response ok 1:")
                      responseListener?.onResponse(objectResponse)

                  }
              }

          }).getAllChannels()
      }



   inner class SaveToLocal<T>(data:T){

        private val userModalData =data as UserModel
        var realm:Realm?=null

        init {
            saveAllData()

        }

        fun saveAllData(){
            println("||  final res Name54 : DBBBB:")
            try {
                deleteuserTable()
                realm = Realm.getDefaultInstance()
                realm?.executeTransaction { realm ->

                    for (user in userModalData.data){
                        val channel = realm.createObject(ChannelsRealmModel::class.java, UUID.randomUUID().toString())
                        channel.avatar=user.avatar
                        channel.email=user.email
                        channel.first_name=user.firstName
                        channel.last_name=user.lastName
                        //channel.ids=user.id.toString()
                    }

                }
            }catch (e:Exception){
                println("||  final res Name54 : errror: ${e.message}")
            }

        }

       fun deleteuserTable(){
           try {
               realm = Realm.getDefaultInstance()
               realm?.executeTransaction { realm ->
                     realm?.where(ChannelsRealmModel::class.java!!)?.findAll()?.deleteAllFromRealm()
               }
           }catch (e:Exception){
               println("||  final res Name54 : errror: ${e.message}")
           }
       }
    }

     class GetFromLocal{
          fun getAllData():RealmResults<ChannelsRealmModel>?{
            var realm:Realm = Realm.getDefaultInstance()
            var usermodalData = realm.where(ChannelsRealmModel::class.java!!)?.findAll()
            return usermodalData
        }
    }

  /*  val res = realm.where(ChannelsRealmModel::class.java!!).equalTo("first_name", "Rox").findAll()
    Log.d("result34543","value ${res.size}")*/

}


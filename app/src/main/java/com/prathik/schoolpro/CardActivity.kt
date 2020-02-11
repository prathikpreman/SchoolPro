package com.prathik.schoolpro

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.prathik.schoolpro.card.realmmodels.CardInfo
import com.prathik.schoolpro.card.realmmodels.RealmUtils
import com.prathik.schoolpro.dataResources.channel.database.model.ChannelsRealmModel
import io.realm.Realm
import java.util.*
import kotlin.math.log

class CardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card)

        val realm : Realm = Realm.getDefaultInstance()

        realm.executeTransaction {

            val card = realm.createObject<CardInfo>(CardInfo::class.java, RealmUtils.getUpdatedPrimaryKeyForCardInfo())
            card.cardNo="654654645"
            card.cardType="visa"
            card.cvv="458"
            card.nameOnCard="Full name"
            card.validThrough="02/05/2029"

        }


        val users = realm.where<CardInfo>(CardInfo::class.java).findAll()


        for (user in users){
            Log.d("value657657","id : ${user.id}")
            Log.d("value657657","value : ${user.cardNo}")

        }


    }
}

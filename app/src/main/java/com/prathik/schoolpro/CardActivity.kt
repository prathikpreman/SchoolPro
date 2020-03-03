package com.prathik.schoolpro

import android.annotation.TargetApi
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.prathik.schoolpro.card.realmmodels.CardInfo
import com.prathik.schoolpro.card.realmmodels.RealmUtils
import com.prathik.schoolpro.dataResources.channel.database.model.ChannelsRealmModel
import com.prathik.schoolpro.util.BiometricCallback
import com.prathik.schoolpro.util.BiometricUtils
import android.os.CancellationSignal
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import com.prathik.schoolpro.adapter.CardAdapter
import com.prathik.schoolpro.util.BiometricCallbackV28
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_card_rv.*
import kotlinx.android.synthetic.main.layout_cardhome.*
import android.R.menu
import android.view.MenuItem
import com.gtomato.android.ui.transformer.CoverFlowViewTransformer
import kotlinx.android.synthetic.main.activity_corousel.*


class CardActivity : AppCompatActivity() {


    lateinit var cardList:ArrayList<CardInfo>
    lateinit var cardAdapter:CardAdapter




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_cardhome)


        addFab.setOnClickListener {
            startActivity(Intent(this,AddCard::class.java))
            finish()
        }

        getAllCards()
        initRv()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){

            R.id.settings ->{

                startActivity(Intent(this,Settings::class.java))
            }
        }
        return true
    }


    private fun getAllCards(){
        cardList= ArrayList()
        val realm : Realm = Realm.getDefaultInstance()
        val cards = realm.where<CardInfo>(CardInfo::class.java).findAll()
        cardList.addAll(cards)

       /* for (user in users){
            Log.d("value657657","id : ${user.id}")
            Log.d("value657657","value : ${user.cardNo}")

        }*/
    }

    private fun initRv(){
         cardAdapter = CardAdapter(cardList,this)
       //  card_rv.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        card_rv.transformer = CoverFlowViewTransformer()
        card_rv.adapter = cardAdapter
        cardAdapter.notifyDataSetChanged()
    }




    override fun onPause() {
        super.onPause()
       finish()
    }



}

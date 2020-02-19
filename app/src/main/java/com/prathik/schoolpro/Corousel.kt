package com.prathik.schoolpro

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.gtomato.android.ui.transformer.FlatMerryGoRoundTransformer
import android.support.v4.app.SupportActivity
import android.support.v4.app.SupportActivity.ExtraData
import android.support.v4.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.gtomato.android.ui.transformer.CoverFlowViewTransformer
import com.gtomato.android.ui.transformer.TimeMachineViewTransformer
import com.prathik.schoolpro.adapter.CardAdapter
import com.prathik.schoolpro.card.realmmodels.CardInfo
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_card_rv.*
import kotlinx.android.synthetic.main.activity_corousel.*


class Corousel : AppCompatActivity() {

    lateinit var cardAdapter:CardAdapter
    lateinit var cardList:ArrayList<CardInfo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_corousel)


        cardList= ArrayList()
        val realm : Realm = Realm.getDefaultInstance()
        val cards = realm.where<CardInfo>(CardInfo::class.java).findAll()
        cardList.addAll(cards)

        cardAdapter = CardAdapter(cardList,this)



        carousel.setTransformer(CoverFlowViewTransformer())
        carousel.setAdapter(cardAdapter)

    }
}

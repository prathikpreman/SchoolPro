package com.prathik.schoolpro.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.prathik.schoolpro.R
import com.prathik.schoolpro.card.realmmodels.CardInfo
import com.wajahatkarim3.easyflipview.EasyFlipView




class CardAdapter( private val cardList: ArrayList<CardInfo>, private val context: Context) :
    RecyclerView.Adapter<CardAdapter.CardHolder>() {

    val cardTypeIcon: IntArray = intArrayOf(
        R.drawable.ic_visa,
        R.drawable.ic_mastercard,
        R.drawable.ic_rupay,
        R.drawable.ic_visa_electron,
        R.drawable.ic_maestro
    )

    val bankIcon: IntArray = intArrayOf(
        R.drawable.allahabad,
        R.drawable.andra,
        R.drawable.axisbank,
        R.drawable.bandan,
        R.drawable.bob,
        R.drawable.boi,
        R.drawable.bom,
        R.drawable.canara,
        R.drawable.catholic,
        R.drawable.cenytralbank,
        R.drawable.cub,
        R.drawable.corporation,
        R.drawable.dcb,
        R.drawable.dena,
        R.drawable.dhanalakshmi,
        R.drawable.federalbank,
        R.drawable.hdfc_bank,
        R.drawable.icici,
        R.drawable.idbi,
        R.drawable.idfc,
        R.drawable.indianbank,
        R.drawable.iob,
        R.drawable.indusind,
        R.drawable.jk,
        R.drawable.karnataka,
        R.drawable.karurvysya,
        R.drawable.kotak,
        R.drawable.lakhsmivilas,
        R.drawable.ninitalbank,
        R.drawable.orientalbank,
        R.drawable.punjabandsynd,
        R.drawable.punjabnational,
        R.drawable.rbl,
        R.drawable.sib,
        R.drawable.sbi,
        R.drawable.syndicate,
        R.drawable.tmb,
        R.drawable.uco,
        R.drawable.unionbank,
        R.drawable.ubi,
        R.drawable.vijaya,
        R.drawable.yesbank
    )

    var skinThumbsId = arrayOf<Int>(
        R.drawable.cardbg_black,
        R.drawable.cardbg_brown,
        R.drawable.cardbg_green,
        R.drawable.cardbg_pink,
        R.drawable.cardbg_red,
        R.drawable.cardbg_violet,
        R.drawable.cardbg_skyblue
    )

    override fun onCreateViewHolder(view: ViewGroup, position: Int): CardAdapter.CardHolder {
        return CardHolder(
            LayoutInflater.from(
                view.context
            ).inflate(R.layout.activity_card, view, false)
        )
    }

    override fun getItemCount(): Int {
        return cardList.size
    }

    override fun onBindViewHolder(holder: CardAdapter.CardHolder, position: Int) {
        var card = cardList[position]
        holder.cardName.text = card.nameOnCard
        holder.cardCVV.text = card.cvv
        holder.cardNumber.text = card.cardNo.substring(0,4)+" "+card.cardNo.substring(4,8)+" "+card.cardNo.substring(8,12)+" "+card.cardNo.substring(12,16)
        holder.cardFlipView.setOnClickListener {
            holder.cardFlipView.flipTheView()
        }

        setCardTypeIcon(card.cardType, holder)
        setBankIcon(card.bankName, holder)
        setCardSkin(card.cardSkin,holder)
    }

    class CardHolder(v: View) : RecyclerView.ViewHolder(v) {

        var cardNumber: TextView = v.findViewById(R.id.raw_cardNumber)
        var cardCVV: TextView = v.findViewById(R.id.raw_cardCVV)
        var cardName: TextView = v.findViewById(R.id.raw_cardName)
        var cardFlipView: EasyFlipView = v.findViewById(R.id.cardFlipView)
        var cardIcon: ImageView = v.findViewById(R.id.cardIcon)
        var bankLogo: ImageView = v.findViewById(R.id.bankLogo)
        var cardSkinLayout: ImageView = v.findViewById(R.id.cardSkinLayout)
    }


    private fun setCardSkin(pos:Int,holder:CardHolder){

        holder.cardSkinLayout.setBackgroundResource(skinThumbsId[pos])


    }

    private fun setCardTypeIcon(type: String, holder: CardAdapter.CardHolder) {
        val res = context.resources
        val cardTypes = res.getStringArray(R.array.cardTypes)

        when (type) {
            cardTypes[0] -> {
                holder.cardIcon.setImageResource(cardTypeIcon[0])
            }
            cardTypes[1] -> {
                holder.cardIcon.setImageResource(cardTypeIcon[1])
            }
            cardTypes[2] -> {
                holder.cardIcon.setImageResource(cardTypeIcon[2])
            }
            cardTypes[3] -> {
                holder.cardIcon.setImageResource(cardTypeIcon[3])
            }
            cardTypes[4] -> {
                holder.cardIcon.setImageResource(cardTypeIcon[4])
            }
        }

    }

    private fun setBankIcon(bank: String, holder: CardAdapter.CardHolder) {
        val res = context.resources
        val bankNames = res.getStringArray(R.array.bankNames)

        for (i in 0 until (bankNames.size - 1)) {

            if (bank == bankNames[i]) {

                holder.bankLogo.setImageResource(bankIcon[i])
            }
        }

    }
}
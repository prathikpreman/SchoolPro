package com.prathik.schoolpro.adapter

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.support.v4.content.ContextCompat.getSystemService
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.prathik.schoolpro.R
import com.prathik.schoolpro.card.realmmodels.CardInfo
import com.prathik.schoolpro.util.decrypt
import com.wajahatkarim3.easyflipview.EasyFlipView




class CardAdapter( private val cardList: ArrayList<CardInfo>, private val context: Context) :
    RecyclerView.Adapter<CardAdapter.CardHolder>() {


    lateinit var notificationManager : NotificationManager
    lateinit var notificationChannel : NotificationChannel
    lateinit var builder : Notification.Builder
    private val channelId = "i.apps.notifications"
    private val description = "Test notification"

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
        holder.cardName.text = card.nameOnCard.decrypt()
        holder.cardCVV.text = card.cvv.decrypt()
        holder.rawExpiry.text= card.validThrough.decrypt()
        var cardNumber=(card.cardNo.decrypt()).substring(0,4)+" "+(card.cardNo.decrypt()).substring(4,8)+" "+(card.cardNo.decrypt()).substring(8,12)+" "+(card.cardNo.decrypt()).substring(12,16)
        holder.cardNumber.text = cardNumber
        holder.cardFlipView.setOnClickListener {
            holder.cardFlipView.flipTheView()
        }

        setCardTypeIcon(card.cardType.decrypt(), holder)



        if(setBankIcon(card.bankName.decrypt())!=null){
            setBankIcon(card.bankName.decrypt())?.let { holder.bankLogo.setImageResource(it) }
            holder.bankLogo.visibility=View.VISIBLE
            holder.otherBankName.visibility=View.GONE
        }else{
            holder.bankLogo.visibility=View.GONE
            holder.otherBankName.visibility=View.VISIBLE
            holder.otherBankName.text=card.bankName.decrypt()
        }


        setCardSkin(card.cardSkin,holder)

        holder.notiBtn.setOnClickListener {
            showNotification(cardNumber,card.validThrough.decrypt())
        }
    }

    class CardHolder(v: View) : RecyclerView.ViewHolder(v) {

        var cardNumber: TextView = v.findViewById(R.id.raw_cardNumber)
        var cardCVV: TextView = v.findViewById(R.id.raw_cardCVV)
        var cardName: TextView = v.findViewById(R.id.raw_cardName)
        var rawExpiry: TextView = v.findViewById(R.id.raw_Expiry)
        var cardFlipView: EasyFlipView = v.findViewById(R.id.cardFlipView)
        var cardIcon: ImageView = v.findViewById(R.id.cardIcon)
        var bankLogo: ImageView = v.findViewById(R.id.bankLogo)
        var cardSkinLayout: ImageView = v.findViewById(R.id.cardSkinLayout)
        var notiBtn: Button = v.findViewById(R.id.notiBtn)
        var otherBankName: TextView = v.findViewById(R.id.otherBankName)
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

    private fun setBankIcon(bank: String):Int? {
        val res = context.resources
        val bankNames = res.getStringArray(R.array.bankNames)

        for (i in 0 until (bankNames.size - 1)) {
            if (bank == bankNames[i]) {
                return bankIcon[i]
            }
        }

        return null
    }



    private fun showNotification(cardNo:String,validThrough:String){
        notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(
                channelId,description,NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(false)
            notificationManager.createNotificationChannel(notificationChannel)

            builder = Notification.Builder(context,channelId)
             //   .setContent(contentView)
                .setContentTitle(cardNo)
                .setContentText(validThrough)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setLargeIcon(
                    BitmapFactory.decodeResource(context.resources,
                    R.drawable.ic_launcher_background))
             //   .setContentIntent(pendingIntent)
        }else{

            builder = Notification.Builder(context)
              //  .setContent(contentView)
                .setContentTitle(cardNo)
                .setContentText(validThrough)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setLargeIcon(BitmapFactory.decodeResource(context.resources,
                    R.drawable.ic_launcher_background))
              //  .setContentIntent(pendingIntent)
        }
        notificationManager.notify(1234,builder.build())
    }
}
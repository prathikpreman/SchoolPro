package com.prathik.schoolpro

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import com.prathik.schoolpro.card.realmmodels.CardInfo
import com.prathik.schoolpro.card.realmmodels.RealmUtils
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_add_card.*
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.widget.Toast


class AddCard : AppCompatActivity() {

    lateinit var cardInfo:CardInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_card)



        setCardMonth()
        setBankNames()
        setCardType()
        setValidThroughText()
        addCardBtnAction()
    }

    private fun addCardBtnAction() {
        addCardBtn.setOnClickListener {
            cardInfo=CardInfo()
            cardInfo.cardType=cardTypeSpinner.selectedItem.toString()
            cardInfo.bankName=bankSpinner.selectedItem.toString()
            cardInfo.validThrough=validThroughText.text.toString()
            cardInfo.nameOnCard=EcarduserName.text.toString()
            cardInfo.cvv=EcardCvv.text.toString()
            cardInfo.cardNo=EcardNumber.text.toString()

            saveCard(cardInfo)
        }
    }


    fun setCardMonth() {


    }

    fun setValidThroughText(){
        validThroughText.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                validThroughText.hint = "00/0000"
            } else {
                validThroughText.hint = ""
            }
        }



        validThroughText.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if(count<before && s?.length==3){
                    validThroughText.setText(s?.substring(0,1))
                }

                else if(s?.length==2){
                    validThroughText.setText("${validThroughText.text}/")
                }
                validThroughText.setSelection(validThroughText.text.length)
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

    }

    fun setBankNames() {
        val banks = resources.getStringArray(R.array.bankNames)

        if (bankSpinner != null) {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_dropdown_item, banks)
            bankSpinner.adapter = adapter
        }
    }

    fun setCardType() {
        val cardTypes = resources.getStringArray(R.array.cardTypes)

        if (cardTypeSpinner != null) {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_dropdown_item, cardTypes
            )
            cardTypeSpinner.adapter = adapter
        }
    }


    fun saveCard(cardInfos:CardInfo){
       val realm : Realm = Realm.getDefaultInstance()

      realm.executeTransaction {

          val card = realm.createObject<CardInfo>(CardInfo::class.java, RealmUtils.getUpdatedPrimaryKeyForCardInfo())
          card.cardNo=cardInfos.cardNo
          card.cardType=cardInfos.cardType
          card.cvv=cardInfos.cvv
          card.nameOnCard=cardInfos.nameOnCard
          card.validThrough=cardInfos.validThrough
          card.bankName=cardInfos.bankName

      }



      val users = realm.where<CardInfo>(CardInfo::class.java).findAll()


      for (user in users){
          Log.d("value657657","id : ${user.id}")
          Log.d("value657657","value : ${user.cardNo}")

      }

        finish()
    }

}

package com.prathik.schoolpro

import android.app.Activity
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
import com.prathik.schoolpro.adapter.CardImageAdapter
import android.content.Intent
import android.widget.AdapterView
import android.widget.Spinner
import com.prathik.schoolpro.util.CToast
import com.prathik.schoolpro.util.decrypt
import com.prathik.schoolpro.util.encrypt

class AddCard : AppCompatActivity(), CardImageAdapter.OnSkinSelectedListener {

    lateinit var cardInfo: CardInfo

    var SKIN_SELECTED_POSITION: Int = 0
    lateinit var cardImageAdapter: CardImageAdapter

    var skinThumbsId = arrayOf(
        R.drawable.cardbg_black,
        R.drawable.cardbg_brown,
        R.drawable.cardbg_green,
        R.drawable.cardbg_pink,
        R.drawable.cardbg_red,
        R.drawable.cardbg_violet,
        R.drawable.cardbg_skyblue
    )

    private fun init() {
        cardImageAdapter = CardImageAdapter(this, skinThumbsId, SKIN_SELECTED_POSITION, this)
        gridview.adapter = cardImageAdapter

    }

    override fun onSkinSelected(position: Int) {
        SKIN_SELECTED_POSITION = position
        cardImageAdapter = CardImageAdapter(this, skinThumbsId, position, this)
        gridview.adapter = cardImageAdapter
        cardImageAdapter.notifyDataSetChanged()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_card)

        val actionbar = supportActionBar
        actionbar?.title = "Add Card"
        actionbar?.setDisplayHomeAsUpEnabled(true)


        init()

        setBankNames()
        setCardType()
        setValidThroughText()
        addCardBtnAction()
    }

    private fun addCardBtnAction() {

        addCardBtn.setOnClickListener {

            when {
                EcardNumber.text.length < 16 -> EcardNumber.error = "Invaid Card Number"
                EcardCvv.text.length < 3 -> EcardCvv.error = "Invaid CVV"
                validThroughText.text.length < 5 -> validThroughText.error = "Expiry should be of format MM/YYYY"
                EcarduserName.text.isEmpty() -> EcarduserName.error = "Card User Name Required"
                else -> {

                    cardInfo = CardInfo()
                    cardInfo.cardType = cardTypeSpinner.selectedItem.toString()
                    if(bankSpinner.selectedItem.toString()=="Other Bank"){
                        cardInfo.bankName=other_bank.text.toString()
                    }else{
                        cardInfo.bankName = bankSpinner.selectedItem.toString()
                    }
                    cardInfo.validThrough = validThroughText.text.toString()
                    cardInfo.nameOnCard = EcarduserName.text.toString()
                    cardInfo.cvv = EcardCvv.text.toString()
                    cardInfo.cardNo = EcardNumber.text.toString()
                    cardInfo.cardSkin = SKIN_SELECTED_POSITION
                    saveCard(cardInfo)
                }
            }
        }
    }

    fun setValidThroughText() {
        validThroughText.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                validThroughText.hint = "MM/YY"
            } else {
                validThroughText.hint = ""
            }
        }

        validThroughText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if (count < before && s?.length == 3) {
                    validThroughText.setText(s?.substring(0, 1))
                } else if (s?.length == 2) {
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
                android.R.layout.simple_spinner_dropdown_item, banks
            )
            bankSpinner.adapter = adapter

        }

       bankSpinner.onItemSelectedListener=object :AdapterView.OnItemSelectedListener{
           override fun onNothingSelected(parent: AdapterView<*>?) {

           }

           override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
               if(banks[position]=="Other Bank"){
                   otherBankLayout.visibility=View.VISIBLE
               }else{
                   otherBankLayout.visibility=View.GONE
               }
           }
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


    fun saveCard(cardInfos: CardInfo) {
        val realm: Realm = Realm.getDefaultInstance()

        realm.executeTransaction {

            val card = realm.createObject<CardInfo>(CardInfo::class.java, RealmUtils.getUpdatedPrimaryKeyForCardInfo())
            card.cardNo = cardInfos.cardNo.encrypt()
            card.cardType = cardInfos.cardType.encrypt()
            card.cvv = cardInfos.cvv.encrypt()
            card.nameOnCard = cardInfos.nameOnCard.encrypt()
            card.validThrough = cardInfos.validThrough.encrypt()
            card.bankName = cardInfos.bankName.encrypt()
            card.bankName = cardInfos.bankName.encrypt()
            card.cardSkin = cardInfos.cardSkin
        }

        val users = realm.where<CardInfo>(CardInfo::class.java).findAll()


        for (user in users) {
            Log.d("value657657", "id : ${user.id}")
            Log.d("value657657", "value : ${user.cardNo.decrypt()}")
        }
        openHome()
    }


    override fun onPause() {
        super.onPause()
        finish()
    }


    override fun onBackPressed() {
        super.onBackPressed()
        openHome()
    }

    override fun onSupportNavigateUp(): Boolean {
        openHome()
        return true
    }

    private fun openHome() {
        startActivity(Intent(this, CardActivity::class.java))
        finish()
    }

}


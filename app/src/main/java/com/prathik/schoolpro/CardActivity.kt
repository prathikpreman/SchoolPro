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
import com.prathik.schoolpro.adapter.CardAdapter
import com.prathik.schoolpro.util.BiometricCallbackV28
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_card_rv.*
import kotlinx.android.synthetic.main.layout_cardhome.*


class CardActivity : AppCompatActivity() {

    lateinit var obj:BiometricUtils
    private var cancellationSignal: CancellationSignal? = null
    lateinit var cardList:ArrayList<CardInfo>
    lateinit var cardAdapter:CardAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_cardhome)




        /*obj= BiometricUtils()
        if(obj.isBiometricPromptEnabled()&& obj.isFingerprintAvailable(this) && obj.isHardwareSupported(this) && obj.isPermissionGranted(this) && obj.isSdkVersionSupported()){
            Toast.makeText(this,"condition OK",Toast.LENGTH_LONG).show()
            displayBiometricPrompt()
        }*/


        addFab.setOnClickListener {
            startActivity(Intent(this,AddCard::class.java))
        }

        getAllCards()
        initRv()

    }

        @TargetApi(Build.VERSION_CODES.P)
        private fun displayBiometricPrompt() {

            val bm = BiometricPrompt.Builder(applicationContext)
                .setTitle("Authenticate")
                .setSubtitle("Place your finger on the fingerprint scanner to login")
               // .setDescription("Add a description")
                .setNegativeButton("CANCEL", mainExecutor, object :DialogInterface.OnClickListener{
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                     finish()
                    }

                })
                .build()
             bm.authenticate(getCancellationSignal(), mainExecutor, getAuthenticationCallback())
        }


    private fun getCancellationSignal(): CancellationSignal? {
        cancellationSignal = CancellationSignal()
        cancellationSignal?.setOnCancelListener(CancellationSignal.OnCancelListener {
            Toast.makeText(this,"Cancelled by signal",Toast.LENGTH_LONG)
        })
        return cancellationSignal
    }


    @TargetApi(Build.VERSION_CODES.P)
    private fun getAuthenticationCallback():BiometricPrompt.AuthenticationCallback {
        return object:BiometricPrompt.AuthenticationCallback() {

           override fun onAuthenticationError(errorCode:Int,
                                      errString:CharSequence) {
               finish()

                super.onAuthenticationError(errorCode, errString)
            }

            override fun onAuthenticationHelp(helpCode:Int,
                                     helpString:CharSequence) {
                super.onAuthenticationHelp(helpCode, helpString)
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
            }


            override fun onAuthenticationSucceeded(
                result:BiometricPrompt.AuthenticationResult) {
                Toast.makeText(this@CardActivity,"Authentication succeed:  ",Toast.LENGTH_LONG).show()
                super.onAuthenticationSucceeded(result)
            }
        }
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
         cardAdapter = CardAdapter(this,cardList)
         card_rv.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        card_rv.adapter = cardAdapter
        cardAdapter.notifyDataSetChanged()
    }


}

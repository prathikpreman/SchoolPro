package com.prathik.schoolpro

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.prathik.schoolpro.util.Authenticator

import kotlinx.android.synthetic.main.activity_two_way_authentication.*

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.CompoundButton

import android.widget.Toast
import com.prathik.schoolpro.util.PreferenceManager


class TwoWayAuthentication : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_two_way_authentication)

        var authenticator=Authenticator()
        var key=authenticator.generateSecretKey()
        Log.d("SecretKey567","Key: ${key}")
        Log.d("SecretKey567 TOTP ","6 Key: ${authenticator.getTOTPCode(key)}")

        var qKey=authenticator.getGoogleAuthenticatorBarCode(key,getString(R.string.app_name),getString(R.string.app_name))

        qrCodeView.setImageBitmap(authenticator.generateQRBitmap(qKey,this))

        setKeyText(key)
        setCheckBox()

    }

    fun setKeyText(key:String){

        qrKeyTextView.text=key
        copyBtn.setOnClickListener{
            val manager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("text", qrKeyTextView.getText())
            manager.primaryClip = clipData
            Toast.makeText(this,"Key Copied !",Toast.LENGTH_LONG).show()
        }
    }

    fun setCheckBox(){
        var isChecked=PreferenceManager.getBoolValue(this,PreferenceManager.TWO_STEP_AUTHENTCATION)
        twostepCheckbox.isChecked=isChecked
         twostepCheckbox.setOnCheckedChangeListener(object :CompoundButton.OnCheckedChangeListener{
             override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                 PreferenceManager.setBoolValue(this@TwoWayAuthentication,PreferenceManager.TWO_STEP_AUTHENTCATION,isChecked)
             }
         })
    }


}

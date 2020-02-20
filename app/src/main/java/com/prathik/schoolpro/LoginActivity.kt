package com.prathik.schoolpro

import android.annotation.TargetApi
import android.content.DialogInterface
import android.content.Intent
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CancellationSignal
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import com.prathik.schoolpro.util.Authenticator
import com.prathik.schoolpro.util.BiometricUtils
import com.prathik.schoolpro.util.PreferenceManager
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    lateinit var obj:BiometricUtils
    private var cancellationSignal: CancellationSignal? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if(PreferenceManager.getBoolValue(this,PreferenceManager.IF_FINGER_PRINT_ENABLED)){
            setFingerPrint()
        }

        setListsnerforPass()

    }

    private fun setListsnerforPass() {
        loginPass.addTextChangedListener(object:TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(loginPass.text.toString()=="1234"){
                   checkTwoStepAuth()
                }
            }

        })
    }

    private fun setFingerPrint() {

        obj= BiometricUtils()
        if(obj.isBiometricPromptEnabled()&& obj.isFingerprintAvailable(this) && obj.isHardwareSupported(this) && obj.isPermissionGranted(this) && obj.isSdkVersionSupported()){
            Toast.makeText(this,"Success",Toast.LENGTH_LONG).show()
            displayBiometricPrompt()
        }
    }

    private fun checkTwoStepAuth() {

      if(PreferenceManager.getBoolValue(this,PreferenceManager.TWO_STEP_AUTHENTCATION)){
            passKeyET.visibility=View.GONE
            twoWayKeyET.visibility=View.VISIBLE

          var authenticator= Authenticator()


          twowayKey.addTextChangedListener(object :TextWatcher{
              override fun afterTextChanged(s: Editable?) {
              }

              override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
              }

              override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                  if(twowayKey.text.toString().length==6 && twowayKey.text.toString()==authenticator.getTOTPCode(PreferenceManager.getStringValue(this@LoginActivity,PreferenceManager.TWO_STEP_AUTHENTCATION_KEY))){

                      goHome()
                  }
                  else if(twowayKey.text.toString().length==6){
                      twowayKey.error="Wrong TOTP"
                  }
              }
          })


        }
        else{
           goHome()
        }

    }

    private fun goHome(){
        startActivity(Intent(this@LoginActivity,CardActivity::class.java))
        finish()
    }


    @TargetApi(Build.VERSION_CODES.P)
    private fun displayBiometricPrompt() {

        val bm = BiometricPrompt.Builder(applicationContext)
            .setTitle("Authenticate")
            .setSubtitle("Place your finger on the fingerprint scanner to login")
            // .setDescription("Add a description")
            .setNegativeButton("CANCEL", mainExecutor, object : DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {

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
    private fun getAuthenticationCallback(): BiometricPrompt.AuthenticationCallback {
        return object: BiometricPrompt.AuthenticationCallback() {

            override fun onAuthenticationError(errorCode:Int,
                                               errString:CharSequence) {

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
                result: BiometricPrompt.AuthenticationResult) {

               checkTwoStepAuth()
                super.onAuthenticationSucceeded(result)
            }
        }
    }
}

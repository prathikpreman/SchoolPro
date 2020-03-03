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
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.prathik.schoolpro.util.Authenticator
import com.prathik.schoolpro.util.BiometricUtils
import com.prathik.schoolpro.util.PreferenceManager
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    lateinit var obj: BiometricUtils
    private var cancellationSignal: CancellationSignal? = null
    lateinit var shake:Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

         shake = AnimationUtils.loadAnimation(this, R.anim.shake);

        if (PreferenceManager.getBoolValue(this, PreferenceManager.IF_FINGER_PRINT_ENABLED)) {
            setFingerPrint()
        }

        setDialerClick()
        setListsnerforPass()

    }

    private fun setListsnerforPass()
    {
        loginPasscode.addTextChangedListener(object : TextWatcher
        {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int)
            {
                if (loginPasscode.length()==4 && loginPasscode.text.toString() == "1234")
                {
                    wrongPinTag.visibility=View.INVISIBLE
                    checkTwoStepAuth()
                }
                else if (loginPasscode.length()==4 && loginPasscode.text.toString() != "1234"){
                    wrongPinTag.visibility=View.VISIBLE
                    wrongPinTag.startAnimation(shake)
                }
                else{
                    wrongPinTag.visibility=View.INVISIBLE
                }
            }
        })
    }

    private fun setFingerPrint() {

        obj = BiometricUtils()
        if (obj.isBiometricPromptEnabled() && obj.isFingerprintAvailable(this) && obj.isHardwareSupported(this) && obj.isPermissionGranted(
                this
            ) && obj.isSdkVersionSupported()
        ) {
            Toast.makeText(this, "Success", Toast.LENGTH_LONG).show()
            displayBiometricPrompt()
        }
    }

    private fun checkTwoStepAuth() {

        if (PreferenceManager.getBoolValue(this, PreferenceManager.TWO_STEP_AUTHENTCATION)) {
            loginPasscode.text=""

            var authenticator = Authenticator()


            loginPasscode.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (loginPasscode.text.toString().length == 6 && loginPasscode.text.toString() == authenticator.getTOTPCode(
                            PreferenceManager.getStringValue(
                                this@LoginActivity,
                                PreferenceManager.TWO_STEP_AUTHENTCATION_KEY
                            )
                        )
                    ) {

                        goHome()
                    } else if (loginPasscode.text.toString().length == 6) {
                        loginPasscode.error = "Wrong TOTP"
                    }
                }
            })
        } else {
            goHome()
        }

    }

    private fun goHome() {
        startActivity(Intent(
            this@LoginActivity,
            CardActivity::class.java
        ))
        finish()
    }


    @TargetApi(Build.VERSION_CODES.P)
    private fun displayBiometricPrompt() {

        val bm = BiometricPrompt.Builder(applicationContext)
            .setTitle("Authenticate")
            .setSubtitle("Place your finger on the fingerprint scanner to login")
            // .setDescription("Add a description")
            .setNegativeButton("CANCEL", mainExecutor, object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {

                }

            })
            .build()
        bm.authenticate(getCancellationSignal(), mainExecutor, getAuthenticationCallback())
    }


    private fun getCancellationSignal(): CancellationSignal? {
        cancellationSignal = CancellationSignal()
        cancellationSignal?.setOnCancelListener(CancellationSignal.OnCancelListener {
            Toast.makeText(this, "Cancelled by signal", Toast.LENGTH_LONG)
        })
        return cancellationSignal
    }


    @TargetApi(Build.VERSION_CODES.P)
    private fun getAuthenticationCallback(): BiometricPrompt.AuthenticationCallback {
        return object : BiometricPrompt.AuthenticationCallback() {

            override fun onAuthenticationError(
                errorCode: Int,
                errString: CharSequence
            ) {

                super.onAuthenticationError(errorCode, errString)
            }

            override fun onAuthenticationHelp(
                helpCode: Int,
                helpString: CharSequence
            ) {
                super.onAuthenticationHelp(helpCode, helpString)
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
            }


            override fun onAuthenticationSucceeded(
                result: BiometricPrompt.AuthenticationResult
            ) {

                checkTwoStepAuth()
                super.onAuthenticationSucceeded(result)
            }
        }
    }


    private fun setDialerClick(){
        key0.setOnClickListener {setPassCodeView(R.string.zero) }
        key1.setOnClickListener {setPassCodeView(R.string.one) }
        key2.setOnClickListener {setPassCodeView(R.string.two) }
        key3.setOnClickListener {setPassCodeView(R.string.three) }
        key4.setOnClickListener {setPassCodeView(R.string.four) }
        key5.setOnClickListener {setPassCodeView(R.string.five) }
        key6.setOnClickListener {setPassCodeView(R.string.six) }
        key7.setOnClickListener {setPassCodeView(R.string.seven) }
        key8.setOnClickListener {setPassCodeView(R.string.eight) }
        key9.setOnClickListener {setPassCodeView(R.string.nine) }
        keyBackspace.setOnClickListener { if(loginPasscode.text.isNotEmpty()){ loginPasscode.text=loginPasscode.text.toString().substring(0,loginPasscode.text.length-1) } }
    }

    private fun setPassCodeView(number:Int){
        if(loginPasscode.text.length<4){
            loginPasscode.text=loginPasscode.text.toString()+getString(number)
        }
    }

    override fun onStop() {
        super.onStop()
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        finish()
    }
}

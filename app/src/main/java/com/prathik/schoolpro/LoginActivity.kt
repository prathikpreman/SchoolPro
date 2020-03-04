package com.prathik.schoolpro

import android.annotation.TargetApi
import android.content.DialogInterface
import android.content.Intent
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import android.os.Bundle
import android.os.CancellationSignal
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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
    lateinit var shake: Animation
    var enteredPassCode: String = ""
    var flag = true
    var PIN_MAX_LENGTH:Int=4

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val actionbar = supportActionBar
        actionbar?.title = getString(R.string.enterpin)

        shake = AnimationUtils.loadAnimation(this, R.anim.shake);



        if (intent.getStringExtra("from") == "settings") {
            actionbar?.title = getString(R.string.setloginpin)
            actionbar?.setDisplayHomeAsUpEnabled(true)
            setPinText.visibility = View.VISIBLE
            setPinText.text = getString(R.string.entercurrentpin)
        }


        if (PreferenceManager.getStringValue(this, PreferenceManager.LOGIN_PIN).isEmpty()) {
            setPin()
        } else if (PreferenceManager.getBoolValue(this, PreferenceManager.IF_FINGER_PRINT_ENABLED)) {
            setFingerPrint()
            setListenerforPass()
        } else {
            setListenerforPass()
        }

        setDialerClick()
    }

    private fun setPin() {
        setPinText.visibility = View.VISIBLE
        setPinText.text = getString(R.string.create4digitpin)

        var tempPin = ""
        loginPasscode.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if (s?.length == PIN_MAX_LENGTH && tempPin.isEmpty()) {
                    tempPin = s.toString()
                    loginPasscode.text = ""
                    setPinText.text = "Re-Enter Pin"
                } else if (s?.length == PIN_MAX_LENGTH && tempPin.isNotEmpty()) {

                    if (s.toString() == tempPin) {
                        PreferenceManager.setStringValue(this@LoginActivity, PreferenceManager.LOGIN_PIN, s.toString())

                        if (intent.getStringExtra("from") == null) {
                            goHome()
                        } else {
                            goBackToSettings()
                        }

                    } else {
                        wrongPinTag.startAnimation(shake)
                        tempPin = ""
                        loginPasscode.text = ""
                        setPinText.text = getString(R.string.create4digitpin)
                    }
                }
            }

        })

    }

    private fun goBackToSettings() {
        startActivity(Intent(this, Settings::class.java))
    }

    private fun setListenerforPass() {
        setPinText.visibility = View.VISIBLE
        setPinText.text = getString(R.string.enterpin)

        loginPasscode.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (flag) {
                    if (count > before && s != null) {
                        enteredPassCode += s.last()
                    } else if (count < before && s != null) {
                        enteredPassCode = enteredPassCode.substring(0, s.length)
                    }
                    Log.d("exp5435", "count : $count")
                    Log.d("exp5435", "before : $before")
                    Log.d("exp5435", "pass : $enteredPassCode")
                    if (count != before) {

                        if (enteredPassCode.length == PIN_MAX_LENGTH && enteredPassCode == PreferenceManager.getStringValue(
                                this@LoginActivity,
                                PreferenceManager.LOGIN_PIN
                            )
                        ) {
                            wrongPinTag.visibility = View.INVISIBLE

                            flag = false
                            if (intent.getStringExtra("from") == null) {
                                checkTwoStepAuth()
                            } else {
                                loginPasscode.text = ""
                                setPin()
                            }

                        } else if (enteredPassCode.length == PIN_MAX_LENGTH && enteredPassCode != PreferenceManager.getStringValue(
                                this@LoginActivity,
                                PreferenceManager.LOGIN_PIN
                            )
                        ) {
                            wrongPinTag.visibility = View.VISIBLE
                            loginPasscode.error = "Wrong PIN"
                            wrongPinTag.startAnimation(shake)
                        } else {
                            wrongPinTag.visibility = View.INVISIBLE
                        }

                        lockPassAnim()
                    }

                }
            }
        })
    }


    private fun lockPassAnim() {
        Log.d("exp5435", "running...")
        val delayMillis: Long = 500
        val handler = Handler()
        handler.postDelayed({
            if (loginPasscode.text.isNotEmpty()) {
                val re = Regex("[0-9]")
                loginPasscode.text = re.replace(loginPasscode.text, "•")
                //  loginPasscode.text= (loginPasscode.text.toString().replace("""[$,.]""".toRegex(), "•"))
            }
        }, delayMillis)

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

        setPinText.text = getString(R.string.google_pin)

        if (PreferenceManager.getBoolValue(this, PreferenceManager.TWO_STEP_AUTHENTCATION)) {
            loginPasscode.text = ""
            loginPasscode.maxLines=6
            PIN_MAX_LENGTH=6

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
                        wrongPinTag.visibility = View.VISIBLE
                        loginPasscode.error = "Wrong TOTP"
                        wrongPinTag.startAnimation(shake)
                    } else if(loginPasscode.text.toString().length < 6){
                        wrongPinTag.visibility = View.INVISIBLE
                        loginPasscode.error = null
                    }
                }
            })
        } else {
            goHome()
        }
    }

    private fun goHome() {
        startActivity(
            Intent(
                this@LoginActivity,
                CardActivity::class.java
            )
        )
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


    private fun setDialerClick() {
        key0.setOnClickListener { setPassCodeView(R.string.zero) }
        key1.setOnClickListener { setPassCodeView(R.string.one) }
        key2.setOnClickListener { setPassCodeView(R.string.two) }
        key3.setOnClickListener { setPassCodeView(R.string.three) }
        key4.setOnClickListener { setPassCodeView(R.string.four) }
        key5.setOnClickListener { setPassCodeView(R.string.five) }
        key6.setOnClickListener { setPassCodeView(R.string.six) }
        key7.setOnClickListener { setPassCodeView(R.string.seven) }
        key8.setOnClickListener { setPassCodeView(R.string.eight) }
        key9.setOnClickListener { setPassCodeView(R.string.nine) }
        keyBackspace.setOnClickListener {
            if (loginPasscode.text.isNotEmpty()) {
                loginPasscode.text = loginPasscode.text.toString().substring(0, loginPasscode.text.length - 1)
            }
        }
        keyBackspace.setOnLongClickListener {
            loginPasscode.text = ""
            return@setOnLongClickListener true
        }
    }

    private fun setPassCodeView(number: Int) {
        if (loginPasscode.text.length < PIN_MAX_LENGTH) {
            loginPasscode.text = loginPasscode.text.toString() + getString(number)
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


    override fun onBackPressed() {
        super.onBackPressed()
        if (intent.getStringExtra("from") == "settings")
            goBackToSettings()
    }


    override fun onSupportNavigateUp(): Boolean {
        goBackToSettings()
        return true
    }

}

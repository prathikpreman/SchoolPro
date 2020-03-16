package com.prathik.schoolpro

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import com.prathik.schoolpro.util.BiometricUtils
import com.prathik.schoolpro.util.PreferenceManager
import kotlinx.android.synthetic.main.activity_settings.*

class Settings : AppCompatActivity() {

    lateinit var obj: BiometricUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val actionbar = supportActionBar
        actionbar?.title = getString(R.string.settings)
        actionbar?.setDisplayHomeAsUpEnabled(true)

        isFingerPrintAvailable()
        setTwoStepAuth()
        setLockPin()
    }

    private fun setTwoStepAuth() {
        twoStepBtn.setOnClickListener {
            startActivity(Intent(this, TwoWayAuthentication::class.java)) }
    }


    private fun isFingerPrintAvailable() {
        obj = BiometricUtils()
        if (!(obj.isHardwareSupported(this))) {  // no hardware
            btn_fingerPrint.visibility = View.GONE
        } else {
            fingerPrintSwitch.isChecked =
            PreferenceManager.getBoolValue(this, PreferenceManager.IF_FINGER_PRINT_ENABLED)

            fingerPrintSwitch.setOnCheckedChangeListener { _, isChecked ->
                PreferenceManager.setBoolValue(this, PreferenceManager.IF_FINGER_PRINT_ENABLED, isChecked)
                if (isChecked && !(obj.isFingerprintAvailable(this))) {
                    Toast.makeText(this, getString(R.string.add_atleast_one_fingerprint), Toast.LENGTH_LONG).show()
                    PreferenceManager.setBoolValue(this, PreferenceManager.IF_FINGER_PRINT_ENABLED, false)
                    fingerPrintSwitch.isChecked = false
                }
            }
        }
    }

    private fun setLockPin() {
        keyLockSetting.setOnClickListener {
            var intent = Intent(this, LoginActivity::class.java)
            intent.putExtra("from", getString(R.string.settings))
            startActivity(intent)
        }
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

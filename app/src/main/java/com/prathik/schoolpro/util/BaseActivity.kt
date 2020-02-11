package com.prathik.schoolpro.util


import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.prathik.schoolpro.webapi.coroutine.CoroutineBase
import kotlinx.coroutines.cancel

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onPause() {
        super.onPause()
        CoroutineBase.instance.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        CoroutineBase.instance.cancel()
    }
}

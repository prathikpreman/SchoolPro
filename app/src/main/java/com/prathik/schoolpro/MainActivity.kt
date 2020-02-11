package com.prathik.schoolpro


import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import kotlinx.coroutines.*
import com.prathik.schoolpro.interfaces.OnHttpResponse
import com.prathik.schoolpro.webapi.WebApi
import com.prathik.schoolpro.webapi.coroutine.CoroutineBase
import com.prathik.schoolpro.webapi.model.UserModel
import com.prathik.schoolpro.dataResources.channel.database.model.ChannelsRealmModel
import io.realm.Realm
import java.util.*
import kotlin.collections.HashMap
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.prathik.schoolpro.dataResources.channel.webapi.ChannelDataSource
import com.prathik.schoolpro.util.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*
import android.webkit.WebChromeClient
import at.huber.youtubeExtractor.VideoMeta
import at.huber.youtubeExtractor.YtFile
import android.util.SparseArray
import at.huber.youtubeExtractor.YouTubeExtractor
import android.support.v4.app.SupportActivity
import android.support.v4.app.SupportActivity.ExtraData
import android.support.v4.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T




class MainActivity : BaseActivity() ,View.OnClickListener {


    val baseUrl="http://68.183.88.149/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        loadInitialUrl()

        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, progress: Int) {
                // update the progressBar

                if(progress>=99){
                    progBar.visibility=View.GONE
                }else{
                    progBar.progress=progress
                }

            }
        }


        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                Log.d("urlloaded456","URL :  $url")

                if(url.contains("html")){
                    Log.d("urlloaded456","now in browser")
                    view.loadUrl(url)
                }
                else if(url.contains("youtube")){
                    Log.d("urlloaded456","now in videoplayer")
                    extractYoutubeUrl(url)
                }
                else{
                    Log.d("urlloaded456","now in videoplayer")
                    var intent= Intent(this@MainActivity,PlayerActivity::class.java)
                    intent.putExtra("url",url)
                    startActivity(intent)
                }
                return true
            }
        }





    }


    fun loadInitialUrl(){
        webView.settings.javaScriptEnabled = true

        val settings = webView.settings
        settings.domStorageEnabled = true

        webView.loadUrl(baseUrl)
    }

    override fun onClick(v: View?) {


    }



    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (event?.action == KeyEvent.ACTION_DOWN) {
            when (keyCode) {
                KeyEvent.KEYCODE_BACK -> {
                    if (webView.canGoBack()) {
                        webView.goBack()
                    } else {
                        finish()
                    }
                    return true
                }
            }

        }
        return super.onKeyDown(keyCode, event)
    }

    private fun extractYoutubeUrl(mYoutubeLink:String) {
        @SuppressLint("StaticFieldLeak") val mExtractor = object:YouTubeExtractor(this) {
             override fun onExtractionComplete(sparseArray:SparseArray<YtFile>, videoMeta:VideoMeta) {
                if (sparseArray != null)
                {
                    Log.d("urlloaded456","now in new youtbe: ${sparseArray.get(22).url} ")

                    var intent= Intent(this@MainActivity,PlayerActivity::class.java)
                    intent.putExtra("url",sparseArray.get(22).url)
                    startActivity(intent)
                }
            }
        }
        mExtractor.extract(mYoutubeLink, true, true)
    }





}



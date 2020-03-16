package com.prathik.schoolpro.vision

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.Toast
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.text.TextBlock
import com.google.android.gms.vision.text.TextRecognizer
import com.prathik.schoolpro.R
import com.prathik.schoolpro.util.CToast
import kotlinx.android.synthetic.main.activity_ocr.*
import java.util.logging.Logger

class OCRActivity : AppCompatActivity() {

    lateinit var textRecognizer:TextRecognizer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ocr)

        initScan()
        startScan() }

    private fun initScan(){
        //  Create text Recognizer
         textRecognizer = TextRecognizer.Builder(this).build()
        if (!textRecognizer.isOperational) {
          //  toast("Dependencies are not loaded yet...please try after few moment!!")
          //  Log.d("Dependencies are downloading....try after few moment")
            return
        }
//  Init camera source to use high resolution and auto focus
        var mCameraSource = CameraSource.Builder(applicationContext, textRecognizer)
            .setFacing(CameraSource.CAMERA_FACING_BACK)
            .setRequestedPreviewSize(1280, 1024)
            .setAutoFocusEnabled(true)
            .setRequestedFps(2.0f)
            .build()

        surface_camera_preview.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceChanged(p0: SurfaceHolder?, p1: Int, p2: Int, p3: Int) {
            }
            override fun surfaceDestroyed(p0: SurfaceHolder?) {
                mCameraSource.stop()
            }
            @SuppressLint("MissingPermission")
            override fun surfaceCreated(p0: SurfaceHolder?) {
                try {
                    if (isCameraPermissionGranted()) {
                        mCameraSource.start(surface_camera_preview.holder)
                    } else {
                       // requestForPermission()
                    }
                } catch (e: Exception) {
                    CToast.show(this@OCRActivity,"Error:  ${e.message}")
                }
            }
        })
    }


    private fun isCameraPermissionGranted():Boolean{
        return true
    }

    private fun startScan(){
         textRecognizer.setProcessor(object : Detector.Processor<TextBlock> {

             override fun release() {}

             override fun receiveDetections(detections: Detector.Detections<TextBlock>) {
                 val items = detections.detectedItems

                 if (items.size() <= 0) {
                     return
                 }

                 tv_result.post {
                     val stringBuilder = StringBuilder()
                     for (i in 0 until items.size()) {
                         val item = items.valueAt(i)
                         stringBuilder.append(item.value)
                         stringBuilder.append("\n")
                     }
                     tv_result.text = stringBuilder.toString()
                 }
             }
         })
    }


}








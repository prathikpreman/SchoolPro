package com.prathik.schoolpro.util

import android.content.Context
import android.widget.Toast

class CToast {

    companion object{
        fun show(context:Context,text:String){
            Toast.makeText(context,text,Toast.LENGTH_LONG).show()
        }
    }

}
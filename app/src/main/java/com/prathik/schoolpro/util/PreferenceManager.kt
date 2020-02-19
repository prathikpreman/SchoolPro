package com.prathik.schoolpro.util

import android.R.id.edit
import android.content.Context
import android.content.SharedPreferences
import android.content.Context.MODE_PRIVATE



object PreferenceManager {


    val TWO_STEP_AUTHENTCATION="TWO_STEP_AUTHENTCATION"




    private fun getSharedPreferences(context: Context,key:String): SharedPreferences {
        return context.getSharedPreferences(key,  MODE_PRIVATE)
    }

    fun getStringValue(context: Context,key:String): String {
        return getSharedPreferences(context,key).getString(key, null)
    }

    fun setStringValue(context: Context,key:String, newValue: String) {
        val editor = getSharedPreferences(context,key).edit()
        editor.putString(key, newValue)
        editor.apply()
    }


    fun getBoolValue(context: Context,key:String): Boolean {
        return getSharedPreferences(context,key).getBoolean(key,false)
    }

    fun setBoolValue(context: Context,key:String, newValue: Boolean) {
        val editor = getSharedPreferences(context,key).edit()
        editor.putBoolean(key, newValue)
        editor.apply()
    }

    fun getIntValue(context: Context,key:String): Int {
        return getSharedPreferences(context,key).getInt(key,0)
    }

    fun setIntValue(context: Context,key:String, newValue: Int) {
        val editor = getSharedPreferences(context,key).edit()
        editor.putInt(key, newValue)
        editor.apply()
    }

}
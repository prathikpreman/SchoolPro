package com.prathik.schoolpro.card.realmmodels

import android.os.AsyncTask.execute
import io.realm.Realm
import android.icu.lang.UCharacter.GraphemeClusterBreak.T



class RealmUtils {


    companion object{

        open fun getUpdatedPrimaryKeyForCardInfo():Int{
            val realm : Realm = Realm.getDefaultInstance()
            var nextId: Int

            var currentIdNum = realm.where(CardInfo::class.java).max("id")

            nextId = if (currentIdNum == null) {
                1
            } else {
                currentIdNum.toInt() + 1
            }

            return nextId
                }

        }
    }


package com.prathik.schoolpro.card.realmmodels

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open  class CardInfo (@PrimaryKey var id: Int = 0,var  nameOnCard:String="",var cardNo:String="",var cardType:String="",var cvv:String="",var validThrough:String="",var bankName:String=""):RealmObject()
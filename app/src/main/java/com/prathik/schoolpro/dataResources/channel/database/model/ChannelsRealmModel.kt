package com.prathik.schoolpro.dataResources.channel.database.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import kotlinx.serialization.Required

open class ChannelsRealmModel(@Required @PrimaryKey var id:String?=null,
                              @Required var first_name:String?=null,
                              var avatar:String?=null,
                              var email:String?=null,
                              var last_name:String?=null) : RealmObject()



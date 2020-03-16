package com.prathik.schoolpro.util

import android.util.Base64


 fun String.decrypt(): String {
    return Base64.decode(this, Base64.DEFAULT).toString(charset("UTF-8"))
}

 fun String.encrypt(): String {
    return Base64.encodeToString(this.toByteArray(charset("UTF-8")), Base64.DEFAULT)
}
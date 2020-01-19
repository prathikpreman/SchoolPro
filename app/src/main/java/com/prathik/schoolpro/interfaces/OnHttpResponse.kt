package com.prathik.schoolpro.interfaces

interface OnHttpResponse {

   open fun<T> onResponse(objectResponse:T)
}
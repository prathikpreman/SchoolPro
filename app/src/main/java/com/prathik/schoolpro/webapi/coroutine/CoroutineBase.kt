package com.prathik.schoolpro.webapi.coroutine

import kotlinx.coroutines.*

class CoroutineBase {



    companion object{

        private val parentJob = Job()

        private val coroutineExceptionHandler: CoroutineExceptionHandler =

            CoroutineExceptionHandler { _, throwable ->
                coroutineScope.launch(Dispatchers.Main) {
                    println("Error")
                }
                GlobalScope.launch { println("Caught $throwable") }
            }

        val coroutineScope = CoroutineScope(Dispatchers.Main + parentJob + coroutineExceptionHandler)




        val instance= coroutineScope
    }



}
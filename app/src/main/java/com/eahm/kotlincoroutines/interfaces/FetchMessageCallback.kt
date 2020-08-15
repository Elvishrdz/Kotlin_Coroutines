package com.eahm.kotlincoroutines.interfaces

import java.lang.Exception

interface FetchMessageCallback{
    fun onCompleted()
    fun onError(error : Exception)
}
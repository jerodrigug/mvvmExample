package com.pragma.dev.mvvmexample.views

interface ResultListener {
    fun onResult(isAdded:Boolean)
    fun onError(error:String)
}
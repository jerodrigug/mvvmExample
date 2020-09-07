package com.pragma.dev.mvvmexample.modelview

import android.util.Log
import androidx.lifecycle.ViewModel
import com.amplifyframework.core.Amplify
import com.pragma.dev.mvvmexample.views.ResultListener

class ViewModel : ViewModel() {

    fun confirmSignIn(username: String, password: String, resultListener: ResultListener) {

        Amplify.Auth.signIn(
            username,
            password,
            { result ->  if(result.isSignInComplete) resultListener.onResult(true)},
            { error -> resultListener.onError(error.cause.toString().substringAfter(':').substringBefore('.')) }
        )
    }

    fun confirmSignOut(){
        Amplify.Auth.signOut(
            { Log.i("login", "Signed out successfully") },
            { error -> Log.e("login", error.toString()) }
        )
    }


    fun checkInput(username:String, password:String) : String {

        if (username.isEmpty() || password.isEmpty()) {
            return "EMPTY"
        }

        if (password.length < 6 ){
            return  "SHORTPASS"
        }

        return "CORRECT"
    }
}
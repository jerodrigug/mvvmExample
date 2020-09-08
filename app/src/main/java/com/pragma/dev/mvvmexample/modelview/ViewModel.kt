package com.pragma.dev.mvvmexample.modelview

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amplifyframework.AmplifyException
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.core.Amplify
import com.pragma.dev.mvvmexample.Status
import com.pragma.dev.mvvmexample.views.ResultListener


class ViewModel : ViewModel() {
    private var status : MutableLiveData<Status> = MutableLiveData(Status.SUCCESS)

    fun authorizeAmplifySession(context: Context){
        try {
            Amplify.addPlugin(AWSCognitoAuthPlugin())
            Amplify.configure(context)
            Amplify.Auth.fetchAuthSession(
                { result -> Log.i("MainActivity", result.toString()) },
                { error -> Log.e("MainActivity", error.toString()) }
            )
            Log.i("MainActivity", "Initialized Amplify in Main Activity")
        } catch (error: AmplifyException) {
            Log.e("MainActivity", "Could not initialize Amplify in Main Activity ", error)
        }
    }

    fun checkInput(username:String, password:String) : LiveData<Status> {

        if (username.isEmpty()) {
            status.value = Status.EMPTY_USERNAME
        }else if(password.isEmpty()) {
            status.value = Status.EMPTY_PASSWORD
        }else if (password.length < 6 ){
            status.value = Status.INVALID_PASSWORD
        }else{
            status.value = Status.SUCCESS
        }
        return status
    }

    fun confirmSignIn(username: String, password: String, resultListener: ResultListener) {

        Amplify.Auth.signIn(
            username,
            password,
            { result -> if(result.isSignInComplete) resultListener.onResult(true) },
            { error -> resultListener.onError(error.toString().substringAfter(':').substringBefore('.'))}
        )
    }

    fun confirmSignOut(){
        Amplify.Auth.signOut(
            { Log.i("login", "Signed out successfully") },
            { error -> Log.e("login", error.toString()) }
        )
    }
}
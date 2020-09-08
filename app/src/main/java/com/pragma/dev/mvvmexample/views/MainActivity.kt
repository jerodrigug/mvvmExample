package com.pragma.dev.mvvmexample.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.amplifyframework.AmplifyException
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.core.Amplify
import com.pragma.dev.mvvmexample.Status
import com.pragma.dev.mvvmexample.databinding.ActivityMainBinding
import com.pragma.dev.mvvmexample.modelview.ViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var model : ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        model = ViewModelProvider(this).get(ViewModel::class.java)
        model.authorizeAmplifySession(applicationContext)

        binding.signinButton.setOnClickListener { checkingInputs() }

    }

    private fun checkingInputs() {
        val username = binding.editTextUsername.text.toString()
        val password = binding.editTextPasswords.text.toString()

        model.checkInput(username,password).observe(this, Observer {
            handleStatus(it,username,password)
        })

    }

    private fun handleStatus(status:Status,username:String, password: String){
        when(status){
            Status.EMPTY_USERNAME -> binding.textErrorLogin.text = "Username can't be empty"
            Status.EMPTY_PASSWORD -> binding.textErrorLogin.text = "Password can't be empty"
            Status.INVALID_PASSWORD -> binding.textErrorLogin.text = "Password must contain at least six chars"
            Status.SUCCESS -> loginUserViewModel(username,password)
            else -> Log.i("MainActivity", "Hubo un problema en la verificación")
        }
    }

   private fun loginUserViewModel(username:String, password:String){

        model.confirmSignIn(username,password, object : ResultListener{
            override fun onResult(isAdded: Boolean) {
                val intent =  Intent(applicationContext, homeActivity::class.java)
                intent.putExtra("username", username)
                startActivity(intent)
            }

            override fun onError(error: String) {
               binding.textErrorLogin.text = error
            }
        })
    }
}
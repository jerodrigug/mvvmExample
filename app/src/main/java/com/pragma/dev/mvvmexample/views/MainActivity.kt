package com.pragma.dev.mvvmexample.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.amplifyframework.AmplifyException
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.core.Amplify
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

        try {
            Amplify.addPlugin(AWSCognitoAuthPlugin())
            Amplify.configure(applicationContext)
            Amplify.Auth.fetchAuthSession(
                { result -> Log.i("MainActivity", result.toString()) },
                { error -> Log.e("MainActivity", error.toString()) }
            )
            Log.i("MainActivity", "Initialized Amplify in Main Activity")
        } catch (error: AmplifyException) {
            Log.e("MainActivity", "Could not initialize Amplify in Main Activity ", error)
        }

        model = ViewModelProvider(this).get(ViewModel::class.java)

        binding.signinButton.setOnClickListener { signinUser() }
    }

    private fun signinUser() {
        val username = binding.editTextUsername.text.toString()
        val password = binding.editTextPasswords.text.toString()

        when(model.checkInput(username,password)){
            "EMPTY" -> binding.textErrorLogin.text = "Username/Password can't be empty"
            "SHORTPASS" -> binding.textErrorLogin.text = "Password must have at least six chars"
            "CORRECT" -> loginUserViewModel(username,password)
            else -> Log.i("login", "algo salió mal con la entrada de datos")
        }
    }

    private fun loginUserViewModel(username:String, password:String){

        model.confirmSignIn(username,password, object : ResultListener{
            override fun onResult(isAdded: Boolean) {
                val intent =  Intent(applicationContext, homeActivity::class.java)
                intent.putExtra("username", username)
                startActivity(intent)
                //Toast.makeText(applicationContext, "el result $isAdded", Toast.LENGTH_SHORT).show()
            }

            override fun onError(error: String) {
               binding.textErrorLogin.text = error
            }
        })
    }
}
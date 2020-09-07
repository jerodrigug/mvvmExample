package com.pragma.dev.mvvmexample.views


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.pragma.dev.mvvmexample.databinding.ActivityHomeBinding
import com.pragma.dev.mvvmexample.modelview.ViewModel

class homeActivity : AppCompatActivity() {

    private lateinit var binding : ActivityHomeBinding
    private lateinit var model: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.textView.text = "Welcome  ${intent.getStringExtra("username").toString()}"
        model = ViewModelProvider(this).get(ViewModel::class.java)
        binding.button.setOnClickListener { logout() }
    }

    fun logout(){
        model.confirmSignOut()
        finish()
    }

    override fun finish() {
        super.finish()
    }
}
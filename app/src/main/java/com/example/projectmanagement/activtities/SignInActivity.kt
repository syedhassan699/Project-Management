package com.example.projectmanagement.activtities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
import com.example.projectmanagement.R
import com.example.projectmanagement.databinding.ActivitySignInBinding

@Suppress("DEPRECATION")
class SignInActivity : AppCompatActivity() {
    private var binding : ActivitySignInBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setupActionBar()

        window.setFlags(
            FLAG_FULLSCREEN,
            FLAG_FULLSCREEN
        )
    }
    private fun setupActionBar(){
        setSupportActionBar(binding?.toolbarSignup)
        val actionBar = supportActionBar
        if (actionBar!= null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_24dp)
        }
        binding?.toolbarSignup?.setNavigationOnClickListener{onBackPressed()}
    }
}
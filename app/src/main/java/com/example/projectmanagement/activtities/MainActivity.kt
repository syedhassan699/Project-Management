package com.example.projectmanagement.activtities


import android.os.Bundle

import android.view.WindowManager

import com.example.projectmanagement.databinding.ActivityMainBinding


@Suppress("DEPRECATION")
class MainActivity : BaseActivity(){

    private var binding : ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

    }
}
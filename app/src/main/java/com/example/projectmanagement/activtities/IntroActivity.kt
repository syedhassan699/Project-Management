package com.example.projectmanagement.activtities

import android.content.Intent
import android.os.Bundle
import com.example.projectmanagement.databinding.ActivityIntroBinding

class IntroActivity : BaseActivity() {
    private var binding : ActivityIntroBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.SignUp?.setOnClickListener{
            startActivity(Intent(this, SignUpActivity::class.java))
        }
        binding?.SignIn?.setOnClickListener{
            startActivity(Intent(this, SignInActivity::class.java))
        }

    }
}
@file:Suppress("DEPRECATION")

package com.example.projectmanagement.activtities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.example.projectmanagement.databinding.ActivityIntroBinding

class IntroActivity : BaseActivity() {
    private var binding : ActivityIntroBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        binding?.SignUp?.setOnClickListener{
            startActivity(Intent(this, SignUpActivity::class.java))
        }
        binding?.SignIn?.setOnClickListener{
            startActivity(Intent(this, SignInActivity::class.java))
        }

    }
}
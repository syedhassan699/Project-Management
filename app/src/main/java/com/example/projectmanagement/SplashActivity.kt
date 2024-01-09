package com.example.projectmanagement

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager.LayoutParams.*
import com.example.projectmanagement.databinding.ActivitySplashBinding

@Suppress("DEPRECATION")
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private var binding : ActivitySplashBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        window.setFlags(
            FLAG_FULLSCREEN,
            FLAG_FULLSCREEN
        )

        val typeFace : Typeface = Typeface.createFromAsset(assets,"carbon bl.otf")
        binding?.SplashScreenText?.typeface = typeFace

        Handler().postDelayed({
            startActivity(Intent(this,IntroActivity::class.java))
            finish()
        }, 2500)

    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}
package com.example.projectmanagement.activtities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.projectmanagement.R
import com.example.projectmanagement.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth

@Suppress("DEPRECATION")
class SignInActivity : BaseActivity() {
    private lateinit var auth: FirebaseAuth
    private var binding : ActivitySignInBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setupActionBar()

        binding?.signInActivityBtn?.setOnClickListener{
            signInRegisteredUser()
        }

        auth  = FirebaseAuth.getInstance()

    }
    private fun setupActionBar(){
        setSupportActionBar(binding?.toolbarSignIn)
        val actionBar = supportActionBar
        if (actionBar!= null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_24dp)
        }
        binding?.toolbarSignIn?.setNavigationOnClickListener{onBackPressed()}
    }
    private fun signInRegisteredUser(){
        val email:String = binding?.tvEmailSignIn?.text.toString().trim{ it <= ' ' }
        val password:String = binding?.tvPasswordSignIn?.text.toString().trim{ it <= ' ' }

        if (validateForm(email,password)){
            showProgressDialog(resources.getString(R.string.please_wait))

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    hideProgressDialog()
                    if (task.isSuccessful) {
                        Toast.makeText(this@SignInActivity, "You have successfully signed In", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@SignInActivity,MainActivity::class.java))
                    } else {
                        Toast.makeText(
                            baseContext,
                            "Authentication failed.",
                            Toast.LENGTH_LONG,
                        ).show()

                    }
                }
        }
    }
    private fun validateForm(email:String,password:String):Boolean{
        return if (TextUtils.isEmpty(email)){
                showErrorSnackBar("Please enter your Email.")
                false
        }
        else if ( TextUtils.isEmpty(password))
        {
            showErrorSnackBar("Please enter Password.")
            false
        }
        else {
            true
        }
    }
    fun signInSuccess() {
        hideProgressDialog()
        startActivity(Intent(this,MainActivity::class.java))
    }
}
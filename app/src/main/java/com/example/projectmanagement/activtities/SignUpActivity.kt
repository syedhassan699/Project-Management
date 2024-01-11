package com.example.projectmanagement.activtities

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowManager
import android.widget.Toast
import com.example.projectmanagement.R
import com.example.projectmanagement.databinding.ActivitySignUpBinding
import com.example.projectmanagement.firebase.FirestoreClass
import com.example.projectmanagement.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

@Suppress("DEPRECATION")
class SignUpActivity : BaseActivity() {

private var binding : ActivitySignUpBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setupActionBar()

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

    }
    @SuppressLint("SuspiciousIndentation")
    private fun setupActionBar(){
        setSupportActionBar(binding?.toolbarSignup)
        val actionBar = supportActionBar
        if (actionBar!= null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_24dp)
        }
            binding?.toolbarSignup?.setNavigationOnClickListener{onBackPressed()}
            binding?.btnSignupActivity?.setOnClickListener {
                registerUser()
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    fun registerUser(){
        val name = binding?.tvName?.text.toString().trim{it<=' '}
        val email= binding?.tvEmail?.text.toString().trim{it<=' '}
        val password= binding?.tvPassword?.text.toString().trim{it<=' '}

        if (validateForm(name,email,password)){
            showProgressDialog(resources.getString(R.string.please_wait))
            FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val firebaseUser: FirebaseUser = task.result!!.user!!
                        val registeredEmail = firebaseUser.email!!
                        val user= User(firebaseUser.uid,name,registeredEmail)
                        FirestoreClass().registerUser(this,user)
                        finish()
                    } else {
                        Toast.makeText(this, "Registration Failed", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
        }
    }
    private fun validateForm(name:String,email:String,password:String):Boolean{
        return when{
            TextUtils.isEmpty(name)->{
                showErrorSnackBar("Please enter your Name.")
                false
            }
            TextUtils.isEmpty(email)->{
                showErrorSnackBar("Please enter your Email.")
                false
            }
            TextUtils.isEmpty(password)->{
                showErrorSnackBar("Please enter Password.")
                false
            }
            else -> {
                true
            }
        }
    }
    fun userRegisteredSuccess(){
        Toast.makeText(
            this,
                    " you have successfully registered ",
            Toast.LENGTH_LONG
        ).show()
        FirebaseAuth.getInstance().signOut()
        finish()
    }
}
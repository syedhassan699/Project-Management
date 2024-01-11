package com.example.projectmanagement.activtities

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.projectmanagement.R
import com.example.projectmanagement.databinding.ActivityBaseBinding
import com.example.projectmanagement.databinding.DailogCustomBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

@Suppress("DEPRECATION")
open class BaseActivity : AppCompatActivity() {
    private var doubleBackToExitPressedOnce = false

    private lateinit var mProgressDialog: Dialog

    private var binding : ActivityBaseBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBaseBinding.inflate(layoutInflater)
        setContentView(binding?.root)
    }
    fun showProgressDialog(text:String){
        mProgressDialog = Dialog(this)

        val dialogBinding = DailogCustomBinding.inflate(layoutInflater)
        mProgressDialog.setContentView(dialogBinding.root)
        dialogBinding.tvBase.text = text

        mProgressDialog.show()
    }
    fun hideProgressDialog(){
        mProgressDialog.dismiss()
    }

    fun getCurrentUserId():String{
        return FirebaseAuth.getInstance().currentUser!!.uid
    }

    fun doubleBackToExit(){
        if (doubleBackToExitPressedOnce){
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please press once Again to Exit",
            Toast.LENGTH_SHORT).show()

        Handler().postDelayed({doubleBackToExitPressedOnce = false}, 2000)
    }

    @SuppressLint("ShowToast", "ResourceType")
    fun showErrorSnackBar(message:String){
        val snackBar = Snackbar.make(findViewById(android.R.id.content)
            ,message,Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view
        snackBarView.setBackgroundColor(ContextCompat.getColor
            (this@BaseActivity, R.color.snackbar_error_color))
        snackBar.show()
    }

}
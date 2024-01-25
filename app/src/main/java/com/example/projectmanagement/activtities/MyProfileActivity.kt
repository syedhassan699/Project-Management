package com.example.projectmanagement.activtities

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.projectmanagement.R
import com.example.projectmanagement.databinding.ActivityMyProfileBinding
import com.example.projectmanagement.firebase.FirestoreClass
import com.example.projectmanagement.models.User
import com.example.projectmanagement.utils.Constants
import com.example.projectmanagement.utils.Constants.PICK_IMAGE_REQUEST_CODE
import com.example.projectmanagement.utils.Constants.READ_STORAGE_PERMISSION_CODE
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

@Suppress("DEPRECATION")
class MyProfileActivity :BaseActivity() {

    private var mSelectedImageFileUri : Uri? = null
    private var bindingMP : ActivityMyProfileBinding? = null
    private var mProfileImageUrl:String = ""
    private lateinit var mUserDetails:User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingMP = ActivityMyProfileBinding.inflate(layoutInflater)
        setContentView(bindingMP?.root)

        setupActionBar()
        FirestoreClass().loadUserData(this)

        bindingMP?.ivProfileUserImg?.setOnClickListener{
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED){
                Constants.showImageChooser(this)
            }else{
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    READ_STORAGE_PERMISSION_CODE)
            }

            bindingMP?.btnUpdate?.setOnClickListener {
                if (mSelectedImageFileUri != null){
                    uploadUserImage()
                }
                else {
                    showProgressDialog(resources.getString(R.string.please_wait))
                    updateUserProfileData()
                }
            }
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == READ_STORAGE_PERMISSION_CODE){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Constants.showImageChooser(this)
            }else{
                Toast.makeText(this, "Oop's You Denied for permission", Toast.LENGTH_LONG).show()
            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK
            && requestCode == PICK_IMAGE_REQUEST_CODE
            && data!!.data != null)
        {
            mSelectedImageFileUri = data.data
                Glide
                    .with(this@MyProfileActivity)
                    .load(mSelectedImageFileUri)
                    .centerCrop()
                    .placeholder(R.drawable.ic_user_place_holder)
                    .into(bindingMP!!.ivProfileUserImg)
        }
    }

    private fun setupActionBar(){
        setSupportActionBar(bindingMP?.toolbarUpdate)
        val actionBar = supportActionBar
        if (actionBar!= null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white)
        }
        bindingMP?.toolbarUpdate?.setNavigationOnClickListener{onBackPressed()}
    }

    fun setUserDataInUi(user:User){
        mUserDetails = user
        Glide
            .with(this@MyProfileActivity)
            .load(user.image)
            .centerCrop()
            .placeholder(R.drawable.ic_user_place_holder)
            .into(bindingMP!!.ivProfileUserImg)

        bindingMP?.etName?.setText(user.name)
        bindingMP?.etEmail?.setText(user.email)
        if (user.mobile!= 0L) {
            bindingMP?.etMobile?.setText(user.mobile.toString())
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun uploadUserImage() {
        showProgressDialog(resources.getString(R.string.please_wait))
        if (mSelectedImageFileUri != null) {
            val sRef: StorageReference = FirebaseStorage.getInstance()
                .reference.child(
                    "USER_IMAGE" +
                            System.currentTimeMillis() + "." +
                            Constants.getFileExtension(this@MyProfileActivity,mSelectedImageFileUri)
                )
            sRef.putFile(mSelectedImageFileUri!!).addOnSuccessListener { taskSnapshot ->
                Log.i(
                    "Firebase Image Url",
                    taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
                )
                taskSnapshot.metadata!!.reference!!
                    .downloadUrl.addOnSuccessListener { uri ->
                        Log.i("Downloadable Image Uri", uri.toString())
                        mProfileImageUrl = uri.toString()
                        updateUserProfileData()
                        hideProgressDialog()
                    }
            }.addOnFailureListener { exception ->
                Toast.makeText(
                    this, exception.message,
                    Toast.LENGTH_LONG
                ).show()
                hideProgressDialog()
            }
        }
    }
    fun profileUpdateSuccess(){
        hideProgressDialog()
        setResult(Activity.RESULT_OK)
        finish()
    }
    private fun updateUserProfileData(){
        val userHashMap = HashMap<String,Any>()

        if (mProfileImageUrl.isNotEmpty() && mProfileImageUrl!= mUserDetails.image){
            userHashMap[Constants.IMAGE] = mProfileImageUrl
        }
        if (bindingMP?.etName.toString() != mUserDetails.name){
            userHashMap[Constants.NAME] = bindingMP?.etName?.text.toString()
        }
        if (bindingMP?.etMobile.toString() != mUserDetails.mobile.toString()){
            userHashMap[Constants.MOBILE] = bindingMP?.etMobile?.text.toString().toLong()
        }
            FirestoreClass().updateUserProfileData(this,userHashMap)
        }
}
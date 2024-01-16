package com.example.projectmanagement.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import com.example.projectmanagement.activtities.MyProfileActivity
import com.example.projectmanagement.utils.Constants.PICK_IMAGE_REQUEST_CODE

object Constants {
    const val USERS: String = "users"
    const val NAME = "name"
    const val MOBILE = "mobile"
    const val IMAGE = "image"
    const val READ_STORAGE_PERMISSION_CODE = 1
    const val PICK_IMAGE_REQUEST_CODE = 2
}

fun showImageChooser(activity: Activity){
    val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
    activity.startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
}

fun getFileExtension(activity: MyProfileActivity, uri: Uri?): String?{
    return MimeTypeMap.getSingleton()
        .getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
}
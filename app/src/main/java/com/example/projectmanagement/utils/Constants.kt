package com.example.projectmanagement.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap

object Constants {
    const val USERS = "users"
    const val BOARDS = "boards"


    const val NAME = "name"
    const val MOBILE = "mobile"
    const val IMAGE = "image"
    const val ASSIGNED_TO = "assignedTo"
    const val DOCUMENT_ID = "documentId"
    const val TASK_LIST = "taskList"
    const val BOARD_DETAIL =  "boardDetail"
    const val ID =  "id"
    const val EMAIL = "email"
    const val BOARDS_MEMBER_LIST = "boardMemberList"
    const val SELECT:String = "Select"
    const val UNSELECT:String = "UnSelect"
    const val PROJECT_PREFERENCE = "ProjectPrefs"
    const val FCM_TOKEN_UPDATED = "fcmTokenUpdated"
    const val FCM_TOKEN = "fcmToken"

    const val FCM_BASE_URL:String = "https://fcm.googleapis.com/fcm/send"
    const val FCM_AUTHORIZATION:String = "authorization"
    const val FCM_KEY:String = "key"
    const val FCM_SERVER_KEY:String = "AAAAXTk6ZRU:APA91bGY5gT5sdecgKx4fQSzGUSz3A_PPHdgpdeK2mDZTcZy7xtnds7UJ-zdWN-dsbLBcdWFFloBgk6it_FJ7jrXxJFe4bkz8W_iByEQunyvQK5g5SuV9wXfVpLr3wItiN8qkNIyqKSA"
    const val FCM_KEY_TITLE:String = "title"
    const val FCM_KEY_MESSAGE:String = "message"
    const val FCM_KEY_DATA:String = "data"
    const val FCM_KEY_TO:String = "to"


    const val READ_STORAGE_PERMISSION_CODE = 1
    const val PICK_IMAGE_REQUEST_CODE = 2
    const val TASK_LIST_ITEM_POSITION = "task_list_item_position"
    const val CARD_LIST_ITEM_POSITION = "card_list_item_position"

    fun showImageChooser(activity: Activity) {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activity.startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }

    fun getFileExtension(activity: Activity, uri: Uri?): String? {
        return MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }
}
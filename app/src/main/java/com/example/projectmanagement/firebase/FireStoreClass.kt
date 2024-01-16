package com.example.projectmanagement.firebase

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.example.projectmanagement.activtities.MainActivity
import com.example.projectmanagement.activtities.MyProfileActivity
import com.example.projectmanagement.activtities.SignInActivity
import com.example.projectmanagement.activtities.SignUpActivity
import com.example.projectmanagement.models.User
import com.example.projectmanagement.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class FirestoreClass {
    private var mFireStore=FirebaseFirestore.getInstance()
    fun registerUser(activity: SignUpActivity,userInfo:User){
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.userRegisteredSuccess()
            }.addOnFailureListener{
                    e->
                Log.e(activity.javaClass.simpleName,"Error writing document")
            }
    }
    fun loadUserData(activity: Activity){
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener {document ->
                val loggedInUser = document.toObject(User::class.java)!!
                when(activity){
                    is SignInActivity  ->{
                        activity.signInSuccess(loggedInUser)
                    }
                    is MainActivity ->{
                        activity.updateNavigationUserDetail(loggedInUser)
                    }
                    is MyProfileActivity->{
                        activity.setUserDataInUi(loggedInUser)
                    }
                }
            }.addOnFailureListener{
                    e->
                when(activity){
                    is SignInActivity  ->{
                        activity.hideProgressDialog()
                    }
                    is MainActivity ->{
                        activity.hideProgressDialog()
                    }
                }
                Log.e(activity.javaClass.simpleName,"Error reading document")
            }
            }
    fun getCurrentUserID(): String {
       val currentUser = FirebaseAuth.getInstance().currentUser
       var currentUserID = ""
        if (currentUser!=null){
            currentUserID = currentUser.uid
        }
        return currentUserID
    }
    fun updateUserProfileData(activity: MyProfileActivity,userHashMap: HashMap<String,Any>){
    mFireStore.collection(Constants.USERS)
        .document(getCurrentUserID())
        .update(userHashMap)
        .addOnSuccessListener {
            Log.i(activity.javaClass.simpleName,"Profile Data is Updated")
            Toast.makeText(activity, "Profile Is Updated Successfully", Toast.LENGTH_SHORT).show()
            activity.profileUpdateSuccess()
        }.addOnFailureListener{
            e -> activity.hideProgressDialog()
            Log.e(activity.javaClass.simpleName,"Profile Update Error")
            Toast.makeText(activity, "Profile not updated Error", Toast.LENGTH_SHORT).show()
        }
}
}
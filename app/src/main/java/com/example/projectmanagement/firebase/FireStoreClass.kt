package com.example.projectmanagement.firebase

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.example.projectmanagement.activtities.CardDetailsActivity
import com.example.projectmanagement.activtities.CreateBoardActivity
import com.example.projectmanagement.activtities.MainActivity
import com.example.projectmanagement.activtities.MemberActivity
import com.example.projectmanagement.activtities.MyProfileActivity
import com.example.projectmanagement.activtities.SignInActivity
import com.example.projectmanagement.activtities.SignUpActivity
import com.example.projectmanagement.activtities.TaskListActivity
import com.example.projectmanagement.models.Board
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
                Log.e(activity.javaClass.simpleName,"Error writing document",e)
            }
    }
    fun loadUserData(activity: Activity,readBoardList:Boolean = false){
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
                        activity.updateNavigationUserDetail(loggedInUser,readBoardList)
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
                Log.e(activity.javaClass.simpleName,"Error reading document",e)
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
            Log.e(activity.javaClass.simpleName,"Profile Update Error",e)
            Toast.makeText(activity, "Profile not updated Error", Toast.LENGTH_SHORT).show()
        }
    }

    fun createBoard(activity: CreateBoardActivity, board: Board){
        mFireStore.collection(Constants.BOARDS)
            .document()
            .set(board, SetOptions.merge())
            .addOnSuccessListener {
                Log.e(activity.javaClass.simpleName,"Board Created Successfully")
                Toast.makeText(activity, "Boards Created Successfully", Toast.LENGTH_SHORT).show()
                activity.boardCreatedSuccessfully()
            }.addOnFailureListener{
                    exception ->
                        activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName,"Error while creating a Board",exception)
            }
    }

    fun getBoardList(activity: MainActivity){
            mFireStore.collection(Constants.BOARDS)
            .whereArrayContains(Constants.ASSIGNED_TO,getCurrentUserID())
            .get()
            .addOnSuccessListener {
                document ->
                Log.i(activity.javaClass.simpleName, document.documents.toString())
                val boardList:ArrayList<Board> = ArrayList()
                for (i in document.documents){
                    val board = i.toObject(Board::class.java)!!
                    board.documentId = i.id
                    boardList.add(board)
                }
               activity.populateBoardListToUI(boardList)
            }.addOnFailureListener{
                e->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName,"Error While Creating A Board",e)
            }
    }

    fun getBoardDetails(activity: TaskListActivity,documentId:String){
            mFireStore.collection(Constants.BOARDS)
                 .document(documentId)
                 .get()
                 .addOnSuccessListener {
                    document ->
                Log.i(activity.javaClass.simpleName, document.toString())
                val board = document.toObject(Board::class.java)!!
                board.documentId = document.id
                activity.boardDetails(board)
            }.addOnFailureListener{
                    e->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName,"Error While Creating A Board",e)
            }
    }

    fun addUpdateTaskList(activity: Activity,board: Board){
        val taskListHashMap = HashMap<String,Any>()
        taskListHashMap[Constants.TASK_LIST] = board.taskList
        mFireStore.collection(Constants.BOARDS)
            .document(board.documentId)
            .update(taskListHashMap)
            .addOnSuccessListener {
                Log.e(activity.javaClass.simpleName,"Task List Updated")

                if (activity is TaskListActivity)
                    activity.addUpdateTaskListSuccess()
                else if (activity is CardDetailsActivity)
                    activity.addUpdateTaskListSuccess()

            }.addOnFailureListener{
                exception ->
                if (activity is TaskListActivity){
                activity.hideProgressDialog()
                }
                if (activity is CardDetailsActivity) {
                    activity.hideProgressDialog()
                }
                Log.e(activity.javaClass.simpleName,
                "Error! while Creating A Board",
                exception)
            }
    }

    fun getAssignedMembersListDetails
    (activity: Activity,
    assignedTo: ArrayList<String>)
    {
        mFireStore.collection(Constants.USERS)
            .whereIn(Constants.ID, assignedTo)
            .get()
            .addOnSuccessListener {
                document ->
                Log.e(activity.javaClass.simpleName,document.documents.toString())

                val usersList:ArrayList<User> = ArrayList()

                for (i in document.documents){
                    val user = i.toObject(User::class.java)!!
                    usersList.add(user)
                }
                if (activity is MemberActivity){
                    activity.setUpMembersList(usersList)
                }
                else if (activity is TaskListActivity){
                    activity.boardMembersDetailList(usersList)
                }

            }.addOnFailureListener{
                e->
                if (activity is MemberActivity){
                    activity.hideProgressDialog()
                }
                else if (activity is TaskListActivity){
                    activity.hideProgressDialog()
                }
                Log.e(activity.javaClass.simpleName,
                    "Error while creating a Board",e)
            }
    }

    fun getMemberDetails(activity: MemberActivity,email:String){
        mFireStore.collection(Constants.USERS)
            .whereEqualTo(Constants.EMAIL, email)
            .get()
            .addOnSuccessListener {
                    document ->
                    if (document.documents.size > 0){
                        val user = document.documents[0].toObject(User::class.java)!!
                        activity.memberDetails(user)
                    }else{
                        activity.hideProgressDialog()
                        activity.showErrorSnackBar("No such Member Found")
                    }

            }.addOnFailureListener{
                    e->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName,
                    "Error while getting the User Details",e)
            }
    }

    fun assignMemberToBoard(activity:MemberActivity,board: Board,user: User){
        val assignedtoHashMap = HashMap<String,Any>()
        assignedtoHashMap[Constants.ASSIGNED_TO] = board.assignedTo

        mFireStore.collection(Constants.BOARDS)
            .document(board.documentId)
            .update(assignedtoHashMap)
            .addOnSuccessListener {
                activity.memberAssignSuccess(user)
            }.addOnFailureListener{
                    e->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName,
                    "Error while getting the User Details",e)
            }
    }
}
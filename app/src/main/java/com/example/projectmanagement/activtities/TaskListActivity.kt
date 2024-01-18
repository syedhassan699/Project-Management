package com.example.projectmanagement.activtities

import android.os.Bundle
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectmanagement.R
import com.example.projectmanagement.adaptors.TaskListItemAdaptor
import com.example.projectmanagement.databinding.ActivityTaskListBinding
import com.example.projectmanagement.firebase.FirestoreClass
import com.example.projectmanagement.models.Board
import com.example.projectmanagement.models.Task
import com.example.projectmanagement.utils.Constants

@Suppress("DEPRECATION")
class TaskListActivity : BaseActivity() {
    private lateinit var mBoardDetails:Board
    private var binding:ActivityTaskListBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskListBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        var boardDocumentId = ""
        if (intent.hasExtra(Constants.DOCUMENT_ID)){
            boardDocumentId = intent.getStringExtra(Constants.DOCUMENT_ID).toString()
        }
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getBoardDetails(this,boardDocumentId)
    }

    private fun setupActionBar(){
        setSupportActionBar(binding?.toolbarTaskListActivity)
        val actionBar = supportActionBar
        if (actionBar!= null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white)
            actionBar.title = mBoardDetails.name
        }
        binding?.toolbarTaskListActivity?.setNavigationOnClickListener{onBackPressed()}
    }
    fun boardDetails(board:Board){
        mBoardDetails = board
        hideProgressDialog()
        setupActionBar()

        val addTaskList = Task(resources.getString(R.string.add_list))
        board.taskList.add(addTaskList)
        binding?.rvTaskList?.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.HORIZONTAL,false)
        binding?.rvTaskList?.setHasFixedSize(true)

        val adapter = TaskListItemAdaptor(this,board.taskList)
        binding?.rvTaskList?.adapter = adapter
    }

    fun addUpdateTaskListSuccess(){
        FirestoreClass().getBoardDetails(this,mBoardDetails.documentId)
    }
}
package com.example.projectmanagement.activtities

import android.app.Dialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectmanagement.R
import com.example.projectmanagement.adaptors.MemberListItemAdaptor
import com.example.projectmanagement.databinding.ActivityMemberBinding
import com.example.projectmanagement.firebase.FirestoreClass
import com.example.projectmanagement.models.Board
import com.example.projectmanagement.models.User
import com.example.projectmanagement.utils.Constants

@Suppress("DEPRECATION")
class MemberActivity : BaseActivity() {
    private lateinit var mBoardDetails: Board
    private lateinit var mAssignedMembersList:ArrayList<User>
    private var binding: ActivityMemberBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMemberBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        if (intent.hasExtra(Constants.BOARD_DETAIL)){
            mBoardDetails = intent.getParcelableExtra(Constants.BOARD_DETAIL)!!
        }
        setupActionBar()
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getAssignedMembersListDetails(this,mBoardDetails.assignedTo)
    }

    private fun setupActionBar(){
        setSupportActionBar(binding?.toolbarMembersActivity)
        val actionBar = supportActionBar
        if (actionBar!= null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white)
            actionBar.title = resources.getString(R.string.members)
        }
        binding?.toolbarMembersActivity?.setNavigationOnClickListener{onBackPressed()}
    }

    fun setUpMembersList(list: ArrayList<User>){

        mAssignedMembersList = list

        hideProgressDialog()
        binding?.rvMembersList?.layoutManager = LinearLayoutManager(this)
        binding?.rvMembersList?.setHasFixedSize(true)

        val adapter = MemberListItemAdaptor(this,list)
        binding?.rvMembersList?.adapter = adapter
    }

    fun memberDetails(user:User){
        mBoardDetails.assignedTo.add(user.id)
        FirestoreClass().assignMemberToBoard(this,mBoardDetails,user)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add_member,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.add_member ->{
                dialogAddMember()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun dialogAddMember(){
        val dialog =  Dialog(this)
        dialog.setContentView(R.layout.dialog_add_member)
        dialog.findViewById<TextView>(R.id.tv_add).setOnClickListener{
            val email=dialog.findViewById<EditText>(R.id.et_email_search_member).text.toString()
            if (email.isNotEmpty()){
                dialog.dismiss()
                showProgressDialog(resources.getString(R.string.please_wait))
                FirestoreClass().getMemberDetails(this,email)
            }
            else{
                Toast.makeText(this, "Please Enter Email Address", Toast.LENGTH_SHORT).show()
            }
        }
        dialog.findViewById<TextView>(R.id.tv_cancel).setOnClickListener{
            dialog.dismiss()
        }
        dialog.show()
    }

    fun memberAssignSuccess(user:User){
        hideProgressDialog()
        mAssignedMembersList.add(user)
        setUpMembersList(mAssignedMembersList)

    }
}
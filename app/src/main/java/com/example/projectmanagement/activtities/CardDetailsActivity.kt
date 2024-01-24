package com.example.projectmanagement.activtities

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.projectmanagement.R
import com.example.projectmanagement.databinding.ActivityCardDetailsBinding
import com.example.projectmanagement.dialogs.LabelColorListDialog
import com.example.projectmanagement.firebase.FirestoreClass
import com.example.projectmanagement.models.Board
import com.example.projectmanagement.models.Card
import com.example.projectmanagement.models.Task
import com.example.projectmanagement.utils.Constants

@Suppress("DEPRECATION")
class CardDetailsActivity : BaseActivity() {
    private var binding:ActivityCardDetailsBinding? = null
    private lateinit var mBoardDetails:Board
    private var mTaskListPosition = -1
    private var mCardPosition = -1
    private var mSelectedColor = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCardDetailsBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        getIntentData()
        setupActionBar()

        binding?.btnUpdateCardDetails?.setOnClickListener { 
            if (binding?.etNameCardDetails?.text!!.isNotEmpty()){
                updateCardDetails()
            }else{
                Toast.makeText(this, "Please Enter Card Name", Toast.LENGTH_SHORT).show()
            }
        }

        mSelectedColor = mBoardDetails.taskList[mTaskListPosition].cards[mCardPosition].labelColor
        if (mSelectedColor.isNotEmpty()){
            setColor()
        }
        binding?.tvSelectLabelColor?.setOnClickListener{
            labelColorListDialog()
        }
    }
    private fun setupActionBar(){
        setSupportActionBar(binding?.toolbarCardDetailsActivity)
        val actionBar = supportActionBar
        if (actionBar!= null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white)
            actionBar.title = mBoardDetails
            .taskList[mTaskListPosition]
            .cards[mCardPosition]
            .name
        }
        binding?.toolbarCardDetailsActivity?.
        setNavigationOnClickListener{onBackPressed()}

        binding?.etNameCardDetails?.setText(mBoardDetails
        .taskList[mTaskListPosition]
        .cards[mCardPosition]
        .name)
        binding?.etNameCardDetails?.setSelection(binding?.
        etNameCardDetails?.
        text.toString().
        length)
    }

    private fun getIntentData(){
        if (intent.hasExtra(Constants.BOARD_DETAIL)){
            mBoardDetails = intent.getParcelableExtra(Constants
                .BOARD_DETAIL)!!
        }
        if (intent.hasExtra(Constants.TASK_LIST_ITEM_POSITION)){
            mTaskListPosition = intent.getIntExtra(Constants
                .TASK_LIST_ITEM_POSITION,-1)
        }
        if (intent.hasExtra(Constants.CARD_LIST_ITEM_POSITION)){
            mCardPosition = intent.getIntExtra(Constants
                .CARD_LIST_ITEM_POSITION,-1)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_delete_member,menu)
        return super.onCreateOptionsMenu(menu)
    }

    fun addUpdateTaskListSuccess(){
        hideProgressDialog()

        setResult(Activity.RESULT_OK)
        finish()
    }

    private fun updateCardDetails(){
        val card = Card(
            binding?.etNameCardDetails?.text.toString(),
            mBoardDetails.taskList[mTaskListPosition].cards[mCardPosition].createdBy,
            mBoardDetails.taskList[mTaskListPosition].cards[mCardPosition].assignedTo,
            mSelectedColor
        )
        mBoardDetails.taskList[mTaskListPosition].cards[mCardPosition] = card

        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().addUpdateTaskList(this@CardDetailsActivity,mBoardDetails)
    }

    private fun deleteCard(){
        val cardsList:ArrayList<Card> = mBoardDetails.
        taskList[mTaskListPosition].cards

        cardsList.removeAt(mCardPosition)

        val taskList: ArrayList<Task> = mBoardDetails.taskList
        taskList.removeAt(taskList.size-1)

        taskList[mTaskListPosition].cards = cardsList
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().addUpdateTaskList(this@CardDetailsActivity, mBoardDetails)
    }

    private fun deleteRecordAlertDialog(cardName:String){
        val builder  = AlertDialog.Builder(this)
        builder.setTitle("Alert")
        builder.setMessage("Are you sure you want to delete $title")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setPositiveButton("Yes"){
                dialogInterface, _ -> dialogInterface.dismiss()
            deleteCard()
        }
        builder.setNegativeButton("No"){
                dialogInterface, _ -> dialogInterface.dismiss()
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.delete_card -> {
                deleteRecordAlertDialog(mBoardDetails.taskList[mTaskListPosition].cards[mCardPosition].name)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun colorsList():ArrayList<String>{
        val colorsList: ArrayList<String> = ArrayList()
        colorsList.add("#43C86F")
        colorsList.add("#0C90F1")
        colorsList.add("#F72400")
        colorsList.add("#7A8089")
        colorsList.add("#D57C1D")
        colorsList.add("#770000")
        colorsList.add("#0022F8")

        return colorsList
    }

    private fun setColor(){
        binding?.tvSelectLabelColor?.text = ""
        binding?.tvSelectLabelColor?.setBackgroundColor(Color.parseColor(mSelectedColor))
    }
    private fun labelColorListDialog(){
        val colorList: ArrayList<String> = colorsList()

        val listDialog = object : LabelColorListDialog(
            this,
            colorList,
            "Selected Label Color",
            mSelectedColor
        ){
            override fun onItemSelected(color: String) {
                mSelectedColor = color
                setColor()
            }
        }
        listDialog.show()
    }
}
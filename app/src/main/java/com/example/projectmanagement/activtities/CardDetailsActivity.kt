package com.example.projectmanagement.activtities

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.projectmanagement.R
import com.example.projectmanagement.adaptors.CardMemberListAdaptor
import com.example.projectmanagement.databinding.ActivityCardDetailsBinding
import com.example.projectmanagement.dialogs.LabelColorListDialog
import com.example.projectmanagement.dialogs.MembersListDialog
import com.example.projectmanagement.firebase.FirestoreClass
import com.example.projectmanagement.models.Board
import com.example.projectmanagement.models.Card
import com.example.projectmanagement.models.SelectedMembers
import com.example.projectmanagement.models.Task
import com.example.projectmanagement.models.User
import com.example.projectmanagement.utils.Constants
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Suppress("DEPRECATION", "NAME_SHADOWING")
class CardDetailsActivity : BaseActivity() {
    private var binding:ActivityCardDetailsBinding? = null
    private lateinit var mBoardDetails:Board
    private lateinit var mMembersDetailList: ArrayList<User>
    private var mTaskListPosition = -1
    private var mCardPosition = -1
    private var mSelectedColor = ""
    private var mSelectedDate:Long = 0
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
        binding?.tvSelectMembers?.setOnClickListener {
            memberListDialog()
        }
        mSelectedDate = mBoardDetails.taskList[mTaskListPosition].cards[mCardPosition].dueDate
        if (mSelectedDate>0){
            val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy",Locale.ENGLISH)
            val selectedDate = simpleDateFormat.format(mSelectedDate)
            binding?.tvSelectDueDate?.text = selectedDate
        }
        binding?.tvSelectDueDate?.setOnClickListener {
            showDataPicker()
        }

        setupSelectedMembersList()
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
        if (intent.hasExtra(Constants.BOARDS_MEMBER_LIST)){
            mMembersDetailList = intent.getParcelableArrayListExtra(Constants.BOARDS_MEMBER_LIST)!!
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
            mSelectedColor,
            mSelectedDate
        )

        val taskList:ArrayList<Task> = mBoardDetails.taskList
        taskList.removeAt(taskList.size -1)


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
    private fun memberListDialog(){
        val cardAssignedMembersList = mBoardDetails
            .taskList[mTaskListPosition]
            .cards[mCardPosition]
            .assignedTo
        if (cardAssignedMembersList.size  > 0){
            for (i in mMembersDetailList.indices){
              for (j in cardAssignedMembersList){
                  if (mMembersDetailList[i].id == j){
                      mMembersDetailList[i].selected = true
                  }
              }
            }
        }else{
            for (i in mMembersDetailList.indices) {
                mMembersDetailList[i].selected = false
            }
        }

        val listDialog = object:MembersListDialog(
            this,
            mMembersDetailList,
            "Select Member"
        ){
            override fun onItemSelected(user: User, action: String) {
               if (action == Constants.SELECT){
                   if (!mBoardDetails
                       .taskList[mTaskListPosition]
                       .cards[mCardPosition]
                       .assignedTo
                       .contains(user.id)){
                       mBoardDetails
                           .taskList[mTaskListPosition]
                           .cards[mCardPosition]
                           .assignedTo.add(user.id)
                   }
               }else{
                   mBoardDetails
                       .taskList[mTaskListPosition]
                       .cards[mCardPosition]
                       .assignedTo.remove(user.id)

                   for (i in mMembersDetailList.indices){
                       if (mMembersDetailList[i].id == user.id){
                           mMembersDetailList[i].selected = false
                       }
                   }
               }

                setupSelectedMembersList()
            }

        }
        listDialog.show()
    }

    private fun setupSelectedMembersList(){
        val cardAssignedMemberList = mBoardDetails
            .taskList[mTaskListPosition]
            .cards[mCardPosition]
            .assignedTo

        val selectedMembersList:ArrayList<SelectedMembers> = ArrayList()
        for (i in mMembersDetailList.indices){
            for (j in cardAssignedMemberList){
                if (mMembersDetailList[i].id == j){
                    val selectedMember = SelectedMembers(
                        mMembersDetailList[i].id,
                        mMembersDetailList[i].image
                    )
                    selectedMembersList.add(selectedMember)
                }
            }
        }

        if (selectedMembersList.size > 0){
            selectedMembersList.add(SelectedMembers("",""))
            binding?.tvSelectMembers?.visibility = View.GONE
            binding?.rvSelectedMembersList?.visibility = View.VISIBLE
            binding?.rvSelectedMembersList?.layoutManager =
                GridLayoutManager(this,6)

            val adaptor = CardMemberListAdaptor(this,selectedMembersList,true)
            binding?.rvSelectedMembersList?.adapter = adaptor
            adaptor.setOnClickListener(
                object : CardMemberListAdaptor.OnClickListener{
                    override fun onClick() {
                        memberListDialog()
                    }
                }
            )

        }
        else{
            binding?.tvSelectMembers?.visibility = View.VISIBLE
            binding?.rvSelectedMembersList?.visibility = View.GONE
        }
    }

    private fun showDataPicker() {

        val c = Calendar.getInstance()
        val year =
            c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val dpd = DatePickerDialog(
            this,
           DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                val sDayOfMonth = if (dayOfMonth < 10) "0$dayOfMonth" else "$dayOfMonth"
                val sMonthOfYear =
                    if ((monthOfYear + 1) < 10) "0${monthOfYear + 1}" else "${monthOfYear + 1}"
                val selectedDate = "$sDayOfMonth/$sMonthOfYear/$year"
                binding?.tvSelectDueDate!!.text = selectedDate
                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
                val theDate = sdf.parse(selectedDate)
                mSelectedDate = theDate!!.time
            },
            year,
            month,
            day
        )
        dpd.show()
    }
}
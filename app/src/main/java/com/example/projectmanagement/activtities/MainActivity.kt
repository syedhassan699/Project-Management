package com.example.projectmanagement.activtities


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.projectmanagement.R
import com.example.projectmanagement.adaptors.BoardItemAdaptor
import com.example.projectmanagement.databinding.ActivityMainBinding
import com.example.projectmanagement.firebase.FirestoreClass
import com.example.projectmanagement.models.Board
import com.example.projectmanagement.models.User
import com.example.projectmanagement.utils.Constants
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import de.hdodenhof.circleimageview.CircleImageView


@Suppress("DEPRECATION")
class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener{

    private var binding : ActivityMainBinding? = null

    private lateinit var mUserName:String

    companion object{
        const val MY_PROFILE_REQUEST_CODE = 11
        const val CREATE_BOARD_REQUEST_CODE = 12
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
       setupActionBar()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )


        binding?.navView?.setNavigationItemSelectedListener (this)
        FirestoreClass().loadUserData(this,true)

        val fb = findViewById<FloatingActionButton>(R.id.floating_action)
        fb.setOnClickListener{
            val intent = Intent(this,CreateBoardActivity::class.java)
            intent.putExtra(Constants.NAME,mUserName)
            startActivityForResult(intent, CREATE_BOARD_REQUEST_CODE)
        }
    }

    private fun setupActionBar(){
        val tb = findViewById<Toolbar>(R.id.toolbar_main_activity)
        setSupportActionBar(tb)
        tb.setNavigationIcon(R.drawable.ic_action_navigation_menu)
        tb.setNavigationOnClickListener {
            toogleDrawer()
        }
    }
    private fun toogleDrawer(){
        val dl = findViewById<DrawerLayout>(R.id.drawer_layout)
        if (dl.isDrawerOpen(GravityCompat.START)){
            dl.closeDrawer(GravityCompat.START)
        }else{
            dl.openDrawer(GravityCompat.START)
        }
    }

    @SuppressLint("MissingSuperCall")
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val dl = findViewById<DrawerLayout>(R.id.drawer_layout)
        if (dl.isDrawerOpen(GravityCompat.START)){
            dl.closeDrawer(GravityCompat.START)
        }
        else{
            doubleBackToExit()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val dl = findViewById<DrawerLayout>(R.id.drawer_layout)
        when (item.itemId) {
            R.id.nav_my_profile -> {
                startActivityForResult(Intent(this,MyProfileActivity::class.java),
                    MY_PROFILE_REQUEST_CODE)
            }

            R.id.nav_sign_out -> {
                FirebaseAuth.getInstance().signOut()

                val intent = Intent(this, IntroActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
        }
        dl.closeDrawer(GravityCompat.START)
        return true
    }
    @SuppressLint("SuspiciousIndentation")
    fun updateNavigationUserDetail(user: User, readBoardList: Boolean){

        mUserName = user.name

    val img = findViewById<CircleImageView>(R.id.nav_user_image)
    val tv = findViewById<TextView>(R.id.tv_username)
        Glide
            .with(this)
            .load(user.image)
            .centerCrop()
            .placeholder(R.drawable.ic_user_place_holder)
            .into(img)

        tv.text = user.name

        if (readBoardList){
            showProgressDialog(resources.getString(R.string.please_wait))
            FirestoreClass().getBoardList(this)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == MY_PROFILE_REQUEST_CODE){
            FirestoreClass().loadUserData(this)
        }else if (resultCode == Activity.RESULT_OK && requestCode == CREATE_BOARD_REQUEST_CODE){
            FirestoreClass().getBoardList(this)
        }
        else{
            Log.e("Cancelled","Loading is Cancelled")
        }
    }
    fun populateBoardListToUI(boardList:ArrayList<Board>){
       hideProgressDialog()
        val rvBoardList=findViewById<RecyclerView>(R.id.rv_board_list)
        val tvNoRecordsAvailable=findViewById<TextView>(R.id.tv_no_board)
        if(boardList.size >0){
            rvBoardList?.visibility= View.VISIBLE
            tvNoRecordsAvailable.visibility= View.GONE
            rvBoardList.layoutManager= LinearLayoutManager(this)
            rvBoardList.setHasFixedSize(true)
            val adapter= BoardItemAdaptor(this,boardList)
            rvBoardList.adapter=adapter

            adapter.setOnClickListener(object :BoardItemAdaptor.OnClickListener{
                override fun onClick(position: Int, model: Board) {
                    val intent = Intent(this@MainActivity,TaskListActivity::class.java)
                    intent.putExtra(Constants.DOCUMENT_ID,model.documentId)
                    startActivity(intent)
                }

            })
        }
        else{
            rvBoardList?.visibility= View.GONE
            tvNoRecordsAvailable.visibility= View.VISIBLE
        }
    }



}
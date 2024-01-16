package com.example.projectmanagement.activtities


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.example.projectmanagement.R
import com.example.projectmanagement.databinding.ActivityMainBinding
import com.example.projectmanagement.firebase.FirestoreClass
import com.example.projectmanagement.models.User
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import de.hdodenhof.circleimageview.CircleImageView


@Suppress("DEPRECATION")
class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener{

    private var binding : ActivityMainBinding? = null

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
        FirestoreClass().loadUserData(this)
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
                startActivity(Intent(this,MyProfileActivity::class.java))
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
    fun updateNavigationUserDetail(user: User){
    val img = findViewById<CircleImageView>(R.id.nav_user_image)
    val tv = findViewById<TextView>(R.id.tv_username)
        Glide
            .with(this)
            .load(user.image)
            .centerCrop()
            .placeholder(R.drawable.ic_user_place_holder)
            .into(img)

        tv.text = user.name
    }
}
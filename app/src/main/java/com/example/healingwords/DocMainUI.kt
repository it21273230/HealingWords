package com.example.healingwords

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import com.example.healingwords.databinding.ActivityDocMainUiBinding
import com.google.firebase.auth.FirebaseAuth

class DocMainUI : AppCompatActivity() {
    private lateinit var mainToolBar: Toolbar
    private lateinit var binding: ActivityDocMainUiBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var fragContainer: FragmentContainerView

    private var isNetworkConnected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDocMainUiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //network status
        val networkConnectivity = NetworkConnectivity(applicationContext)
        networkConnectivity.observe(this){
            val isConnected = it
            if (isConnected && !isNetworkConnected){

                // Show "Connected" Toast only if the network was disconnected and now connected
                Toast.makeText(this, "Connected", Toast.LENGTH_LONG).show()
            }else if (!isConnected && isNetworkConnected){

                Toast.makeText(this, "Connection Lost", Toast.LENGTH_LONG).show()
            }
            isNetworkConnected = isConnected
        }

        mAuth = FirebaseAuth.getInstance()

        mainToolBar = findViewById(R.id.mainToolBar)
        setSupportActionBar(mainToolBar)

        supportActionBar?.title = "Healing Words"

        val bottomNav = binding.docBottomNav
        fragContainer = binding.DocMainUIFragmentContainerView

        //fragments
        val fragmentHome = HomeFragment()
        val fragmentReviews = ShowAllReviews(docSpecified = true, docUid = mAuth.currentUser!!.uid,isDoctor = true)
        val fragmentBlogs = BlogFragment()
        val fragmentDocProfile = DocProfile()

        replaceFragment(fragmentHome)

        //navigation profile page
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_action_home_doc -> {
                    replaceFragment(fragmentHome)
                    true
                }
                R.id.bottom_action_blogs_doc -> {
                    replaceFragment(fragmentBlogs)
                    true
                }
                R.id.bottom_action_account_doc -> {
                    replaceFragment(fragmentDocProfile)
                    true
                }
                R.id.bottom_action_reviews_doc -> {
                    replaceFragment(fragmentReviews)
                    true
                }
                else -> false
            }
        }
    }
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(fragContainer.id, fragment)
            .commit()
    }

    override fun onStart() {
        super.onStart()

        val currentUser = FirebaseAuth.getInstance().currentUser
        if(currentUser == null){
            sendToLogin()
        }
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.main_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.actionLogoutBtn -> {
                logOut()
            }
        }


        return true
    }

    private fun logOut() {

        mAuth.signOut()
        sendToLogin()

    }

    private fun sendToLogin() {
        val loginIntent = Intent(this, LoginPage::class.java)
        startActivity(loginIntent)
        finish()
    }
}
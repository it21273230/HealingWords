package com.example.healingwords

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    private lateinit var mainToolBar: Toolbar

    private lateinit var mAuth: FirebaseAuth

    //private lateinit var  addPostBtn: FloatingActionButton
    private lateinit var mainBottomNav: BottomNavigationView

    private lateinit var homeFragment: HomeFragment
    private lateinit var blogFragment: BlogFragment
    private lateinit var accountFragment: AccountFragment
    private lateinit var docListFragment: DisplayDocList

    private var isNetworkConnected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

        if(mAuth.currentUser != null) {
            mainBottomNav = findViewById(R.id.mainBottomNav)

        //FRAGMENTS
        homeFragment = HomeFragment()
        blogFragment = BlogFragment()
        accountFragment = AccountFragment()
        docListFragment = DisplayDocList()

        replaceFragment(homeFragment)

        mainBottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_action_home -> {
                    replaceFragment(homeFragment)
                    true
                }
                R.id.bottom_action_blogs -> {
                    replaceFragment(blogFragment)
                    true
                }
                R.id.bottom_action_account -> {
                    replaceFragment(accountFragment)
                    true
                }
                R.id.bottom_action_doc_list -> {
                    replaceFragment(docListFragment)
                    true
                }
                else -> false
            }
        }
            /*
            addPostBtn = findViewById(R.id.addPostBtn)
            addPostBtn.setOnClickListener {
                val newpostIntent = Intent(this, NewPostActivity::class.java)
                startActivity(newpostIntent)
            }
            */

            // Check if the "openFragment" extra has been passed
            if (intent.hasExtra("openFragment")) {
                val openFragment = intent.getStringExtra("openFragment")
                if (openFragment == "account") {
                    // Navigate to the AccountFragment
                    replaceFragment(accountFragment)
                }
            }

        }
    }

    override fun onStart() {
        super.onStart()

        val currentUser = FirebaseAuth.getInstance().currentUser;
        val uid = currentUser?.uid
        if(currentUser == null){
            sendToLogin()
        }

        var doctorDatabase = FirebaseDatabase.getInstance().getReference("Doctors")
        if (uid != null) {
            doctorDatabase.child(uid).get().addOnSuccessListener { Doctor ->
                if(Doctor.exists()) {
                    Log.d("status", "exists")
                    sendToDoctorMain(uid)
                }
            }.addOnFailureListener{
                Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun sendToDoctorMain(uid: String) {
        val mainIntent = Intent(this, DocMainUI::class.java)
        mainIntent.putExtra("uid", uid)
        startActivity(mainIntent)
        finish()
    }

    private fun sendToNormalUserMain(uid:String) {
        val mainIntent = Intent(this, MainActivity::class.java)
        mainIntent.putExtra("uid", uid)
        startActivity(mainIntent)
        finish()
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
            R.id.actionSettingsBtn -> {
                val accountIntent = Intent(this, AccountSettings::class.java)
                startActivity(accountIntent)

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
        startActivity(loginIntent);
        finish();
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, fragment)
            .commit()
    }

}
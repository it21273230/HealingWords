package com.example.healingwords

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var mainToolBar: Toolbar

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()

        mainToolBar = findViewById(R.id.mainToolBar)
        setSupportActionBar(mainToolBar)

        supportActionBar?.setTitle("Healing Words")


    }

    override fun onStart() {
        super.onStart()

        val currentUser = FirebaseAuth.getInstance().currentUser;
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
        startActivity(loginIntent);
        finish();
    }
}
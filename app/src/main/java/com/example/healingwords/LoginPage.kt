package com.example.healingwords

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginPage : AppCompatActivity() {

    private lateinit var loginEmailText: EditText
    private lateinit var loginPasswordText: EditText
    private lateinit var submitLoginBtn: Button
    private lateinit var loginRegBtn: Button

    private lateinit var mAuth: FirebaseAuth

    private lateinit var loginProgress: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)

        mAuth = FirebaseAuth.getInstance()

        loginEmailText = findViewById(R.id.editEmailLogin)
        loginPasswordText = findViewById(R.id.editPasswordLogin)
        submitLoginBtn = findViewById(R.id.submitLoginBtn)
        loginRegBtn = findViewById(R.id.loginRegBtn)
        loginProgress = findViewById(R.id.loginProgress)

        loginRegBtn.setOnClickListener {
            val regIntent = Intent(this, RegisterPage::class.java)
            startActivity(regIntent)
        }

        submitLoginBtn.setOnClickListener {


            val loginEmail = loginEmailText.text.toString()
            val loginPass = loginPasswordText.text.toString()

            if (loginEmail.isNotEmpty() && loginPass.isNotEmpty()) {
                loginProgress.visibility = View.VISIBLE

                mAuth.signInWithEmailAndPassword(loginEmail, loginPass)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            sendToMain()
                        } else {
                            val errorMessage = task.exception?.message
                            Toast.makeText(this@LoginPage, "Error: $errorMessage", Toast.LENGTH_LONG).show()


                        }

                        loginProgress.visibility = View.INVISIBLE
                    }
            }
        }
    }

    override fun onStart() {
        super.onStart()

        val currentUser = mAuth.currentUser

        if(currentUser != null){

            sendToMain()

        }

    }

    private fun sendToMain() {
        val mainIntent = Intent(this, MainActivity::class.java)
        startActivity(mainIntent)
        finish()
    }

}
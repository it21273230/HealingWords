package com.example.healingwords

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


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
            val regIntent = Intent(this, ChooseRegType::class.java)
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
                            val currentFirebaseUser = FirebaseAuth.getInstance().currentUser
                            if(currentFirebaseUser != null) {
                                redirectUsers(currentFirebaseUser.uid)
                            }
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
        val currentFirebaseUser = FirebaseAuth.getInstance().currentUser
        if(currentFirebaseUser != null){
            redirectUsers(currentFirebaseUser.uid)
        }
    }


    private fun redirectUsers(uid:String) {
        var a = getUserType(uid)

        Log.d("uid", uid)
        Log.d("type", a.toString())
        if(getUserType(uid).equals("normalUser")) {
            sendToNormalUserMain(uid)
        } else {
            sendToDoctorMain(uid)
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

    private fun getUserType(uid: String):String {
        var userType : String = "";
        var doctorDatabase = FirebaseDatabase.getInstance().getReference("Doctor")
        doctorDatabase.child(uid).get().addOnSuccessListener { Doctor ->
            if(Doctor.exists()) {
                userType = "doctor"
                Log.d("returned", userType.toString())

            }else {
                userType = "normalUser"
                Log.d("returned", userType.toString())
            }
        }.addOnFailureListener{
            Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show()
            userType ="error"
            Log.d("returned", userType.toString())
        }

        return userType
    }


}
package com.example.healingwords

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.example.healingwords.models.Doctor
import com.example.healingwords.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class RegisterPage : AppCompatActivity() {

    private lateinit var regUsername : EditText
    private lateinit var regEmail : EditText
    private lateinit var regPassword : EditText
    private lateinit var regRePassword : EditText
    private lateinit var regProgressBar: ProgressBar
    private lateinit var regButton: Button
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var mAuth: FirebaseAuth
    lateinit var userDatabaseRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_page)

        mAuth = FirebaseAuth.getInstance()
        firebaseFirestore = FirebaseFirestore.getInstance()
        regUsername = findViewById(R.id.edtUserNameRegister)
        regEmail = findViewById(R.id.edtEmail)
        regPassword = findViewById(R.id.edtPasswordRegister)
        regRePassword = findViewById(R.id.edtRePasswordRegister)
        regProgressBar = findViewById(R.id.registerProgress)
        regButton = findViewById(R.id.registerBtn)

        regButton.setOnClickListener {

            val username = regUsername.text.toString()
            val email = regEmail.text.toString()
            val password = regPassword.text.toString()
            val rePassword = regRePassword.text.toString()


            if (username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && rePassword.isNotEmpty()) {
                if(password == rePassword){
                    regProgressBar.visibility = View.VISIBLE
                    mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {

                                val currentFirebaseUser = FirebaseAuth.getInstance().currentUser

                                if (currentFirebaseUser != null) {
                                    currentFirebaseUser.uid?.let { id ->
                                        val userMap = hashMapOf(
                                            "name" to username
                                        )
                                        firebaseFirestore.collection("Users").document(id)
                                            .set(userMap)
                                            .addOnCompleteListener { task ->
                                                if (task.isSuccessful) {
                                                    //
                                                } else {
                                                    val error = task.exception?.message
                                                    Toast.makeText(this, "FIRESTORE error $error", Toast.LENGTH_LONG).show()
                                                }
                                            }
                                    }
                                }

                                if(currentFirebaseUser != null) {
                                    userDatabaseRef = FirebaseDatabase.getInstance().getReference("Users")
                                    val user = User(currentFirebaseUser.uid,username, email)
                                    userDatabaseRef.child(currentFirebaseUser.uid).setValue(user).addOnSuccessListener {

                                        sendToMain()
                                    }.addOnFailureListener {
                                        Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show()
                                    }

                                }



                            } else {
                                val errorMessage = task.exception?.message
                                Toast.makeText(this, "Error: $errorMessage", Toast.LENGTH_LONG).show()


                            }

                            regProgressBar.visibility = View.INVISIBLE
                        }
                }else{
                    Toast.makeText(this, "passwords doesn't match", Toast.LENGTH_LONG).show()
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
        val mainIntent = Intent (this, MainActivity::class.java)
        startActivity(mainIntent)
        finish()
    }


}
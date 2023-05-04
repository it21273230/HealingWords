package com.example.healingwords


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import de.hdodenhof.circleimageview.CircleImageView;






class AccountSettings : AppCompatActivity() {

    private lateinit var ToolBar: Toolbar
    private lateinit var AccountImage: CircleImageView

    private lateinit var setupName: EditText
    private lateinit var setupBtn: Button
    private lateinit var deleteAccBtn: Button

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var userDatabaseRef: DatabaseReference
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_settings)

        ToolBar = findViewById(R.id.accountToolBar)
        setSupportActionBar(ToolBar)
        supportActionBar?.setTitle("Account Setup")

        firebaseAuth = FirebaseAuth.getInstance()
        val user = FirebaseAuth.getInstance().currentUser

        userId = firebaseAuth.currentUser?.uid ?: ""

        firebaseFirestore = FirebaseFirestore.getInstance()
        userDatabaseRef = FirebaseDatabase.getInstance().reference

        AccountImage = findViewById(R.id.accountImage)
        setupName = findViewById(R.id.setupName)
        setupBtn = findViewById(R.id.setupBtn)
        deleteAccBtn = findViewById(R.id.deleteUserAccBtn)

//        firebaseFirestore.collection("Users").document(userId).get()
//            .addOnCompleteListener{ task ->
//                if(task.isSuccessful){
//                    if(task.result.exists()){
//                        var name = task.result.getString("name")
//
//                        setupName.setText(name)
//                    }
//                }else{
//                    val error = task.exception?.message
//                    Toast.makeText(this, "FIRESTORE retrieve error $error", Toast.LENGTH_LONG).show()
//                }
//
//            }

//        setupBtn.setOnClickListener {
//            var username = setupName.text.toString()
//
//            userId = firebaseAuth.currentUser?.uid ?: ""
//
//            if (username.isNotEmpty()) {
//                userId?.let { id ->
//                    val userMap = hashMapOf(
//                        "name" to username
//                    )
//                    firebaseFirestore.collection("Users").document(id)
//                        .set(userMap)
//                        .addOnCompleteListener { task ->
//                            if (task.isSuccessful) {
//                                Toast.makeText(this, "Settings updated", Toast.LENGTH_LONG).show()
//                                val mainIntent = Intent(this, MainActivity::class.java)
//                                startActivity(mainIntent)
//                                finish()
//                            } else {
//                                val error = task.exception?.message
//                                Toast.makeText(this, "FIRESTORE error $error", Toast.LENGTH_LONG).show()
//                            }
//                        }
//                }
//            }
//
//
//        }

        deleteAccBtn.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(this)

            // Set the message for the dialog box
            dialogBuilder.setMessage("Are you sure you want to delete your account? This action cannot be undone.")

            // Set a positive button and its click listener
            dialogBuilder.setPositiveButton("Yes") { _, _ ->
                // User clicked the Yes button, proceed with account deletion
                userDatabaseRef.child("Users").child(userId).removeValue().addOnCompleteListener{task ->
                    if (task.isSuccessful){
                        user?.delete()
                            ?.addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(this, "Account deleted", Toast.LENGTH_LONG).show()
                                    val intent = Intent(this, LoginPage::class.java)
                                    startActivity(intent)
                                    finish()
                                } else {
                                    Toast.makeText(this, "Account deletion failed", Toast.LENGTH_LONG).show()
                                }
                            }
                    }
                }
            }

            // Set a negative button and its click listener
            dialogBuilder.setNegativeButton("No") { _, _ ->
                // User clicked the No button, do nothing
            }

            // Create and show the dialog box
            val dialog = dialogBuilder.create()
            dialog.show()
        }


        userDatabaseRef.child("Users").child(userId).get().addOnCompleteListener{ task ->
            if(task.isSuccessful){
                val dataSnapshot = task.result
                if(dataSnapshot.exists()){
                    val username = dataSnapshot.child("username").value.toString()
                    setupName.setText(username)
                }
            } else{
                val error = task.exception?.message
                Toast.makeText(this, "Realtime Database retrieve error $error", Toast.LENGTH_LONG).show()
            }
        }



        setupBtn.setOnClickListener {
            val username = setupName.text.toString()

            userId = firebaseAuth.currentUser?.uid ?: ""


            if (username.isNotEmpty()) {
                userId?.let { id ->
                    val userMap = hashMapOf<String, Any>(
                        "username" to username
                    )
                    userDatabaseRef.child("Users").child(id)
                        .updateChildren(userMap)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this, "Settings updated", Toast.LENGTH_LONG).show()
                                val mainIntent = Intent(this, MainActivity::class.java)
                                startActivity(mainIntent)
                                finish()
                            } else {
                                val error = task.exception?.message
                                Toast.makeText(this, "Realtime Database error $error", Toast.LENGTH_LONG).show()
                            }
                        }
                }
            }

            if (username.isNotEmpty()) {
                userId?.let { id ->
                    val userMap = hashMapOf(
                        "name" to username
                    )
                    firebaseFirestore.collection("Users").document(id)
                        .set(userMap)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this, "Settings updated", Toast.LENGTH_LONG).show()
                                val mainIntent = Intent(this, MainActivity::class.java)
                                startActivity(mainIntent)
                                finish()
                            } else {
                                val error = task.exception?.message
                                Toast.makeText(this, "FIRESTORE error $error", Toast.LENGTH_LONG).show()
                            }
                        }
                }
            }
        }




        AccountImage.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show()
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)


                } else {
                    //profile image select yet to be implemented
                }
            }
        }

    }
}
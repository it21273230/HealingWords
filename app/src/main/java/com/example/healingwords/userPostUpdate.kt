package com.example.healingwords

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class userPostUpdate : AppCompatActivity() {

    private lateinit var postDesc: EditText
    private lateinit var updateBtn: Button
    private lateinit var deleteBtn: Button
    private lateinit var postId: String
    private lateinit var ToolBar: Toolbar

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseFirestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_post_update)

        ToolBar = findViewById(R.id.postUpdateToolbar)
        setSupportActionBar(ToolBar)
        supportActionBar?.setTitle("Update post")

        postDesc = findViewById(R.id.editPostDesc)
        updateBtn = findViewById(R.id.userPostUpdateBtn)
        deleteBtn = findViewById(R.id.userPostDeleteBtn)
        postId = intent.getStringExtra("postId") ?: ""
        firebaseFirestore = FirebaseFirestore.getInstance()

        firebaseFirestore.collection("Posts").document(postId).get()
            .addOnCompleteListener{ task ->
                if(task.isSuccessful){
                    if(task.result.exists()){
                        var desc = task.result.getString("desc")

                        postDesc.setText(desc)
                    }
                }else{
                    val error = task.exception?.message
                    Toast.makeText(this, "FIRESTORE retrieve error $error", Toast.LENGTH_LONG).show()
                }

            }
        updateBtn.setOnClickListener {
            val newDesc = postDesc.text.toString()

            val updates = hashMapOf(
                "desc" to newDesc
            ) as MutableMap<String, Any>

            firebaseFirestore.collection("Posts").document(postId)
                .update(updates)
                .addOnSuccessListener {
                    Toast.makeText(this, "Post updated successfully", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("openFragment", "account")
                    startActivity(intent)
                    finish()

                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error updating post: $e", Toast.LENGTH_SHORT).show()
                }
        }

        deleteBtn.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Delete Post")
            builder.setMessage("Are you sure you want to delete this post?")
            builder.setPositiveButton("Yes") { dialog, which ->
                // User clicked Yes button
                firebaseFirestore.collection("Posts").document(postId)
                    .delete()
                    .addOnSuccessListener {
                        Toast.makeText(this, "Post deleted successfully", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MainActivity::class.java)
                        intent.putExtra("openFragment", "account")
                        startActivity(intent)
                        finish()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Error deleting post: $e", Toast.LENGTH_SHORT).show()
                    }
            }
            builder.setNegativeButton("No") { dialog, which ->
                // User clicked No button
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()

        }



    }
}
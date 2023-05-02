package com.example.healingwords

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CommentUpdate : AppCompatActivity() {

    private lateinit var commentId: String
    private lateinit var dbref: DatabaseReference
    private lateinit var CommentRecyclerView: RecyclerView
    private lateinit var commentArrayList: ArrayList<Comment>
    private lateinit var commentBody: EditText
    private lateinit var commentdb : DatabaseReference
    private lateinit var updatebtn : Button
    private lateinit var deletebtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment_update)

        commentBody = findViewById(R.id.commentBodyUpdate)
        updatebtn = findViewById(R.id.CUpdate)
        deletebtn = findViewById(R.id.CDelete)
        commentdb = FirebaseDatabase.getInstance().reference
        commentId = intent.getStringExtra("commentId") ?: ""

        commentdb.child("comments").child(commentId).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val dataSnapshot = task.result
                if (dataSnapshot.exists()) {
                    val comment = dataSnapshot.child("commentBody").value.toString()
                    commentBody.setText(comment)
                }
            } else {
                val error = task.exception?.message
                Toast.makeText(this, "Realtime Database retrieve error $error", Toast.LENGTH_LONG)
                    .show()
            }
        }

        updatebtn.setOnClickListener {
            val commment = commentBody.text.toString()


            if (commment.isNotEmpty()) {
                commentId?.let { id ->
                    val userMap = hashMapOf<String, Any>(
                        "commentBody" to commment
                    )
                    commentdb.child("comments").child(id)
                        .updateChildren(userMap)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this, "comment updated", Toast.LENGTH_LONG).show()
                                val mainIntent = Intent(this, MainActivity::class.java)
                                mainIntent.putExtra("openFragment", "account")
                                startActivity(mainIntent)
                                finish()
                            } else {
                                val error = task.exception?.message
                                Toast.makeText(
                                    this,
                                    "Realtime Database error $error",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                }
            }


        }

        deletebtn.setOnClickListener{
            commentId?.let { id ->
            commentdb.child("comments").child(id).removeValue().addOnSuccessListener {
                Toast.makeText(this, "comment Deleted", Toast.LENGTH_LONG).show()
                val DeleteIntent = Intent(this, MainActivity::class.java)
                DeleteIntent.putExtra("openFragment", "account")
                startActivity(DeleteIntent)
            }
            }
        }
    }
}
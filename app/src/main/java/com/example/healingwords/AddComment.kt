package com.example.healingwords

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.healingwords.databinding.ActivityAddCommentBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddComment : AppCompatActivity() {

    private lateinit var postId: String
    private lateinit var userId: String
    private lateinit var binding: ActivityAddCommentBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        postId = intent.getStringExtra("postId") ?: ""
        userId = intent.getStringExtra("userId") ?: ""

        binding.Add.setOnClickListener{

            val comment = binding.commentBody.text.toString()


            database = FirebaseDatabase.getInstance().getReference("comments")

            val commentId = database.push().key
            val Comment = Comment(commentId, comment, userId, postId )
            database.child(commentId!!).setValue(Comment).addOnSuccessListener {

                binding.commentBody.text.clear()

                Toast.makeText(this, "Added", Toast.LENGTH_SHORT).show()

            }.addOnFailureListener{
                Toast.makeText(this, "fail to add", Toast.LENGTH_SHORT).show()

            }
            val intent = Intent(this, ViewComments::class.java)
            intent.putExtra("postId", postId)
            intent.putExtra("userId", userId)
            startActivity(intent)
            finish()

        }
    }
}
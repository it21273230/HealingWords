package com.example.healingwords

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ViewComments : AppCompatActivity() {

    private lateinit var postId: String
    private lateinit var userId: String

    private lateinit var AddCommentbtn: FloatingActionButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_comments)


        postId = intent.getStringExtra("postId") ?: ""
        userId = intent.getStringExtra("userId") ?: ""

        AddCommentbtn = findViewById(R.id.AddCommentbtn)



        AddCommentbtn.setOnClickListener{
            Log.d("postId", postId)

            val intent = Intent(this, AddComment::class.java)
            intent.putExtra("postId", postId)
            intent.putExtra("userId", userId)
            startActivity(intent)
        }


    }
}
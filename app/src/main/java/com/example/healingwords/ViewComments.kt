package com.example.healingwords

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ViewComments : AppCompatActivity() {

    private lateinit var postId: String
    private lateinit var userId: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_comments)


        postId = intent.getStringExtra("postId") ?: ""
        userId = intent.getStringExtra("userId") ?: ""


    }
}
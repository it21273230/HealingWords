package com.example.healingwords

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AllDocBlogs : AppCompatActivity() {

    private lateinit var btnAddBlog : FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_doc_blogs)

       btnAddBlog = findViewById(R.id.AddBlog)

        btnAddBlog.setOnClickListener {
            val intent = Intent(this,AddBlog::class.java)
            startActivity(intent)
            finish()
        }




    }
}
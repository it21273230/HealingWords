package com.example.healingwords

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth

class ShowAllReviewsActivity : AppCompatActivity() {

    private lateinit var fabAddReviews: FloatingActionButton
    private lateinit var docUid: String
    private lateinit var userUid: String
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_all_reviews)

        mAuth = FirebaseAuth.getInstance()
        fabAddReviews = findViewById(R.id.fabAddReview)
        docUid = intent.getStringExtra("docUid")!!
        var currentUser = FirebaseAuth.getInstance().currentUser;
        userUid = currentUser!!.uid

        fabAddReviews.setOnClickListener {
            var intent = Intent(this, AddReview::class.java)
            intent.putExtra("docUid", docUid)
            intent.putExtra("userUid", userUid)
            startActivity(intent)
            finish()
        }
    }
}
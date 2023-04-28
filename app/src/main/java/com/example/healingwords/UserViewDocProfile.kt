package com.example.healingwords

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UserViewDocProfile : AppCompatActivity() {

    lateinit var btnReviews: Button
    private lateinit var tvName: TextView
    private lateinit var tvRating: TextView
    private lateinit var tvBio: TextView
    private lateinit var tvTitle: TextView
    private lateinit var uid: String
    private lateinit var database : DatabaseReference



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_view_doc_profile)

        btnReviews = findViewById(R.id.btnViewReviewsUserView)

        tvName = findViewById(R.id.tvDocNameUserView)
        tvBio = findViewById(R.id.tvDocBioUserView)
        tvRating = findViewById(R.id.tvTotalRatingDocProfileUserView)
        tvTitle =findViewById(R.id.tvDocTitleUserView)

        uid = intent.getStringExtra("uid").toString()

        if(uid.isNotEmpty()) {
            readData(uid)
        }

        btnReviews.setOnClickListener {
            val intent = Intent(this, ShowReviews::class.java)
            intent.putExtra("docUid", uid)
            startActivity(intent)
        }



    }

    private fun readData(uid: String) {
        database = FirebaseDatabase.getInstance().getReference("Doctors")
        database.child(uid).get().addOnSuccessListener {
            if(it.exists()) {
                var name = it.child("name").value
                var title = it.child("title").value
                var bio = it.child("bio").value
                var rating = it.child("rating").value

                if(rating == null){
                    rating=0
                }

                tvName.text = name.toString()
                tvBio.text = bio.toString()
                tvRating.text = "${rating.toString()}/10"
                tvTitle.text = title.toString()

            }else {
                Toast.makeText(applicationContext, "User doesn't exists", Toast.LENGTH_LONG).show()
            }
        }.addOnFailureListener{
            Toast.makeText(applicationContext, "Failed", Toast.LENGTH_LONG).show()
        }
    }

}
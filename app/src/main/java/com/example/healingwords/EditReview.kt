package com.example.healingwords

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast
import com.example.healingwords.databinding.ActivityEditReviewBinding
import com.example.healingwords.models.Doctor
import com.example.healingwords.models.Review
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class EditReview : AppCompatActivity() {
    private lateinit var dbRef: DatabaseReference
    private lateinit var description: String
    private var stars: Int = 0
    private lateinit var userUid: String
    private lateinit var docUid: String
    private lateinit var reviewId: String
    private lateinit var noOfStars: RatingBar
    private lateinit var edtDescription: EditText
    private lateinit var btnSubmit: Button
    private lateinit var btnCancel: Button
    private lateinit var binding: ActivityEditReviewBinding

    private lateinit var docName: String
    private lateinit var username: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        description = intent.getStringExtra("description")!!
        stars = intent.getIntExtra("stars", 0)
        userUid = intent.getStringExtra("userUid")!!
        docUid = intent.getStringExtra("docUid")!!
        reviewId = intent.getStringExtra("reviewId")!!

        noOfStars = binding.editRating2
        edtDescription = binding.edtEditMultilineFeedback2
        btnCancel = binding.cancelEditReview
        btnSubmit = binding.submitEditRating

        noOfStars.rating = stars.toFloat()
        noOfStars.setIsIndicator(false)
        edtDescription.setText(description)

        dbRef = FirebaseDatabase.getInstance().getReference("Reviews")
        FirebaseDatabase.getInstance().getReference("Doctors").child(docUid).get().addOnSuccessListener { doc ->
            if(doc.exists()) {
                docName = doc.child("name").value.toString()
                FirebaseDatabase.getInstance().getReference("Users").child(userUid).get().addOnSuccessListener { user ->
                    if(user.exists()) {
                        username = user.child("username").value.toString()
                    }
                }
            }

        }


        btnSubmit.setOnClickListener {
            val desc = edtDescription.text.toString()
            val numStars = noOfStars.rating.toInt()
            val review = Review(userUid = userUid, docUid = docUid, reviewId = reviewId, description = desc, noOfStars = numStars, username = username!!, docName = docName!!)
            dbRef.child(reviewId).setValue(review).addOnSuccessListener {
                Toast.makeText(applicationContext,"Updated Successfully", Toast.LENGTH_LONG).show()
                val intent = Intent(this, ShowReviews::class.java)
                intent.putExtra("docUid", docUid)
                startActivity(intent)
                finish()
            }.addOnFailureListener {
                Toast.makeText(applicationContext,"Failed to update", Toast.LENGTH_LONG).show()
            }
        }

        btnCancel.setOnClickListener {
            val intent = Intent(this, ShowReviews::class.java)
            intent.putExtra("docUid", docUid)
            startActivity(intent)
            finish()
        }

    }
}
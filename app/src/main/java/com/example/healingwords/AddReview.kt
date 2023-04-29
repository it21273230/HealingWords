package com.example.healingwords

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast
import com.example.healingwords.databinding.ActivityAddReviewBinding
import com.example.healingwords.models.Review
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class AddReview : AppCompatActivity() {
    private lateinit var docUid : String
    private lateinit var userUid: String
    private lateinit var binding: ActivityAddReviewBinding
    private lateinit var description: EditText
    private lateinit var btnSubmit: Button
    private lateinit var btnCancel: Button
    private lateinit var dbRef : DatabaseReference
    private lateinit var noOfStars: RatingBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        docUid = intent.getStringExtra("docUid")!!
        userUid = intent.getStringExtra("userUid")!!

        description = binding.edtAddMultilineFeedback2
        btnCancel = binding.cancelAddReview
        btnSubmit = binding.submitAddRating
        noOfStars = binding.editRating2

        btnSubmit.setOnClickListener{
            dbRef = FirebaseDatabase.getInstance().getReference("Reviews")
            var uniqueID = UUID.randomUUID().toString()
            if(description.text.isNotEmpty()) {
                var rating = noOfStars.rating!!.toInt()
                if(noOfStars.rating == null) {
                    rating = 0
                }
                val review = Review(uniqueID, docUid,userUid, description.text.toString(), rating )
                dbRef.child(uniqueID).setValue(review).addOnSuccessListener {
                    Toast.makeText(this,"Successfully Saved", Toast.LENGTH_LONG).show()

                    var intent = Intent(this, ShowReviews::class.java)
                    intent.putExtra("docUid", docUid)
                    startActivity(intent)
                    finish()
                }.addOnFailureListener {
                    Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, "Please Fill in all the fields", Toast.LENGTH_LONG).show()
            }
        }

        btnCancel.setOnClickListener {
            var intent = Intent(this, ShowReviews::class.java)
            intent.putExtra("docUid", docUid)
            startActivity(intent)
            finish()
        }
    }
}
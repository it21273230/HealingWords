package com.example.healingwords

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import com.example.healingwords.databinding.ActivityEditReviewBinding
import com.google.firebase.database.DatabaseReference

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

    }
}
package com.example.healingwords.ratingCalculation

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.TextView
import com.example.healingwords.models.Review
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RatingCalc() {
    fun calculate(docUid: String, reviewDbRef: DatabaseReference): Int {
        var totStars = 0.0
        var totGivenStars = 0.0
        var noOfReviews = 0.0
        var rate =0

        reviewDbRef.addValueEventListener(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (reviewSnapshot in snapshot.children) {
                        val review = reviewSnapshot.getValue(Review::class.java)
                        Log.d("uid", (review!!.docUid == docUid).toString())

                        if (review!!.docUid == docUid) {
                            noOfReviews += 1.0
                            totStars += 5.0
                            totGivenStars += review.noOfStars!!.toDouble()

                        }

                    }

                }
                val finalRating = ((totGivenStars / (noOfReviews)))
                rate = if (finalRating.isNaN()) {

                    0;
                } else {

                    finalRating.toInt()
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        return rate
    }
}
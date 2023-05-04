package com.example.healingwords.ratingCalculation

import android.annotation.SuppressLint
import android.util.Log
import android.widget.TextView
import com.example.healingwords.models.Review
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RatingsCalcAndSetTV {
    fun calculate(docUid: String, tvRating:TextView): TextView {
        var totStars = 0.0
        var totGivenStars = 0.0
        var noOfReviews = 0.0
        var rate =0.0
        var reviewDbRef = FirebaseDatabase.getInstance().getReference("Reviews")
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
                if (finalRating.isNaN()) {
                    tvRating.setText("0.0/5")
                    rate = 0.0;
                } else {
                    tvRating.setText("$finalRating/5")
                    rate = finalRating
                }



            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        return tvRating
    }

}

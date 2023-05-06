package com.example.healingwords.ratingCalculation

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.TextView
import com.example.healingwords.R
import com.example.healingwords.models.Review
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RatingsCalcAndSetTV {
    fun calculate(docUid: String, activity:Context): String {
        var totStars = 0.0
        var totGivenStars = 0.0
        var noOfReviews = 0.0
        var rate = "null"
        var nulled = true

        val tv = TextView(activity)


        var simulateDatabase = arrayListOf<Review>()
        var r1 = Review(reviewId = "1", docUid = "CK2dLXaVTBYzwMi1SrEW1KA2M8n1", noOfStars = 5)
        var r2 = Review(reviewId = "2", docUid = "CK2dLXaVTBYzwMi1SrEW1KA2M8n1", noOfStars = 5)
        var r3 = Review(reviewId = "3", docUid = "sdKpaEW1KA2M8nKasYzwMi1Sr1", noOfStars = 3)
        simulateDatabase.add(r1)
        simulateDatabase.add(r2)
        simulateDatabase.add(r3)

        for(review in simulateDatabase) {
            if (review!!.docUid == docUid) {
                noOfReviews += 1.0
                totStars += 5.0
                totGivenStars += review.noOfStars!!.toDouble()
                nulled = false
            }

        }

        val finalRating = ((totGivenStars / (noOfReviews)))

        if (finalRating.isNaN()) {
            tv.setText("0.0/5")
        } else {
            tv.setText("$finalRating/5")

        }
        if(!nulled)
            rate = tv.text.toString()

        return rate
    }

}

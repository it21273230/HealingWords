package com.example.healingwords

import com.example.healingwords.ratingCalculation.RatingCalc
import com.google.firebase.database.FirebaseDatabase
import org.junit.Assert.assertEquals
import org.junit.Test

class RatingCalculationTest {
    val rating = RatingCalc()

    var reviewDbRef = FirebaseDatabase.getInstance().getReference("Reviews")

    @Test
    fun testCal() {
        assertEquals(0, rating.calculate("No Id", reviewDbRef))
    }
}
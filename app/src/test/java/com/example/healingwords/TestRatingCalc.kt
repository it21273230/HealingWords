package com.example.healingwords

import com.example.healingwords.ratingCalculation.RatingCalc
import org.junit.Assert.assertEquals
import org.junit.Test

class TestRatingCalc {
    private val rating = RatingCalc()

    @Test
    fun testRating() {
        assertEquals(0.0, rating.calculate(docUid = "CK2dLXaVTBYzwMi1SrEW1KA2M8n1"))
    }

}
package com.example.healingwords

import com.example.healingwords.ratingCalculation.RatingCalc
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.junit.Assert.assertEquals
import org.junit.Test

class RatingCalculationTest {
    val rating = RatingCalc()

    var reviewDbRef = FirebaseDatabase.getInstance().getReference("Reviews")

    // correct uid
    @Test
    fun testCase1() {
        assertEquals(4.5, rating.calculate("CK2dLXaVTBYzwMi1SrEW1KA2M8n1", reviewDbRef))
    }

    // invalid uid
    @Test
    fun testCase2() {
        assertEquals(0, rating.calculate("invalid Doc id", reviewDbRef))
    }

    // empty string as uid
    @Test
    fun testCase3() {
        assertEquals(0, rating.calculate("", reviewDbRef))
    }

    // valid different uid
    @Test
    fun testCase4() {
        assertEquals(3.5, rating.calculate("sdKpaEW1KA2M8nKasYzwMi1Sr1", reviewDbRef))
    }

    // wrong database
    @Test
    fun testCase5() {
        val userDB = FirebaseDatabase.getInstance().getReference("User")
        assertEquals(0, rating.calculate("No Id", userDB))
    }
}
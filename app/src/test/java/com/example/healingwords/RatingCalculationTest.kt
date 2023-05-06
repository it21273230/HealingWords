package com.example.healingwords

import com.example.healingwords.ratingCalculation.RatingCalc
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class RatingCalculationTest {
    private val rating = RatingCalc()
    // correct uid
    @Test
    fun testCase1() {
        assertEquals(5.0, rating.calculate("CK2dLXaVTBYzwMi1SrEW1KA2M8n1"), 0.0001)
    }

    // invalid uid
    @Test
    fun testCase2() {
        assertEquals(0.0, rating.calculate("invalid Doc id"),0.0001)
    }

    // empty string as uid
    @Test
    fun testCase3() {
        assertEquals(0.0, rating.calculate(""),0.0001)
    }

    // valid different uid
    @Test
    fun testCase4() {
        assertEquals(3.0, rating.calculate("sdKpaEW1KA2M8nKasYzwMi1Sr1"),0.0001)
    }

    // Validate return type
    @Test
    fun testCase5() {
        assertNotEquals(3, rating.calculate("sdKpaEW1KA2M8nKasYzwMi1Sr1"))
    }
}
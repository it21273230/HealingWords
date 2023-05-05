package com.example.healingwords.doc_profile.tests

import android.content.Context
import android.content.Intent
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.healingwords.AddReview
import com.example.healingwords.UserViewDocProfile
import org.junit.*
import com.example.healingwords.R
import com.example.healingwords.models.Review
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import junit.framework.TestCase.assertEquals
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class AddReviewsTest {
    private lateinit var tvDesc : TextView
    private lateinit var rating : RatingBar
    private lateinit var btnSubmit: Button
    private lateinit var Activity : AddReview
    private lateinit var appContext: Context
    @Test
    fun useAppContext() {
        // Context of the app under test.
        appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.healingwords", appContext.packageName)
    }

    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(AddReview::class.java)
    private lateinit var activityScenario: ActivityScenario<AddReview>
    @Before
    fun setUp() {
        activityScenario = activityScenarioRule.scenario
        activityScenario = activityScenarioRule.scenario
        activityScenario.onActivity { activity ->
            Activity = activity
            btnSubmit = Activity.requireViewById(R.id.submitAddRating)
            rating = Activity.requireViewById(R.id.editRating2)
            tvDesc = Activity.requireViewById(R.id.edtAddMultilineFeedback2)
            val intent = Intent(Activity, AddReview::class.java)
            intent.putExtra("docUid", "UHBIu81e0LMPfl3auKZKsKtFkh13")
            intent.putExtra("docName", "Chamithu")
            intent.putExtra("userUid", "4O67xchXWUcEZRHollOiuYVQP382")
            intent.putExtra("username", "user99")
            Activity.startActivity(intent)
        }
    }
    @Test
    fun setReview() {
        var rate = 4.0
        rating.rating = rate.toFloat()
        tvDesc.setText("This is a test review")
        btnSubmit.performClick()

        var db = FirebaseDatabase.getInstance().getReference("Reviews")
        db.get().addOnSuccessListener {
            if(it.exists()) {
               val review: Review = it.value as Review
                if(review.description == "This is a test review") {
                    assertEquals(review.noOfStars , rate.toInt())
                }
            }
        }
    }
    @After
    fun tearDown() {
        activityScenario.close()
    }
}
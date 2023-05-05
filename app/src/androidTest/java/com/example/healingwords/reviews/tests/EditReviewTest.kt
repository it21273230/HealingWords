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
import com.example.healingwords.EditReview
import com.example.healingwords.UserViewDocProfile
import org.junit.*
import com.example.healingwords.R
import com.example.healingwords.models.Review
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import junit.framework.TestCase.assertEquals
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class EditReviewTest {
    private lateinit var tvDesc : TextView
    private lateinit var rating : RatingBar
    private lateinit var btnSubmit: Button
    private lateinit var Activity : EditReview
    private lateinit var appContext: Context
    @Test
    fun useAppContext() {
        // Context of the app under test.
        appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.healingwords", appContext.packageName)
    }

    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(EditReview::class.java)
    private lateinit var activityScenario: ActivityScenario<EditReview>

    @Before
    fun setUp() {
        activityScenario = activityScenarioRule.scenario
        activityScenario = activityScenarioRule.scenario
        activityScenario.onActivity { activity ->
            Activity = activity
            btnSubmit = Activity.requireViewById(R.id.submitEditRating)
            rating = Activity.requireViewById(R.id.editRating2)
            tvDesc = Activity.requireViewById(R.id.edtAddMultilineFeedback2)
            val intent = Intent(Activity, AddReview::class.java)
            intent.putExtra("reviewId", "05a54d85-71ce-4ccc-9069-0c10009383c5")
            intent.putExtra("description", "Thankyou")
            intent.putExtra("stars", 5)
            intent.putExtra("userUid", "DZuJl23UEDcbmvgGNjsD5fHIDXF2")
            intent.putExtra("docUid", "TJWuQAXOd9f1qCaykmAhX7BGci12")
            Activity.startActivity(intent)
        }
    }
    @Test
    fun editReview() {
        var db = FirebaseDatabase.getInstance().getReference("Reviews")
        db.child("05a54d85-71ce-4ccc-9069-0c10009383c5").get().addOnSuccessListener {
            if(it.exists()) {
               val description = it.child("description").value
                assertEquals(description, tvDesc.text)
            }
        }
    }
    @After
    fun tearDown() {
        activityScenario.close()
    }

}
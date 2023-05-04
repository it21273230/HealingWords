package com.example.healingwords.doc_profile.tests

import android.content.Context
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.healingwords.DocMainUI
import com.example.healingwords.UserViewDocProfile
import org.junit.*
import com.example.healingwords.R
import com.example.healingwords.ratingCalculation.RatingCalc
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import junit.framework.TestCase.assertEquals
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class RatingTest {
    private lateinit var auth: FirebaseAuth
    private lateinit var uid:String
    private lateinit var docUid:String
    private var rating:String ? = null
    private lateinit var Activity : UserViewDocProfile

    private lateinit var appContext: Context
    @Test
    fun useAppContext() {
        // Context of the app under test.
        appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.healingwords", appContext.packageName)
    }

    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(DocMainUI::class.java)
    private lateinit var activityScenario: ActivityScenario<DocMainUI>



    @Before
    fun setUp() {
        activityScenario = activityScenarioRule.scenario
    }

    @Test
    fun getUid() {
        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid!!

    }



    @Test
    fun testRatings() {
        getUid()
        val rate = RatingCalc()
        val finalRating = rate.calculate(uid)

        val activityScenarioRuleUser = ActivityScenarioRule(UserViewDocProfile::class.java)
        val activityScenario = activityScenarioRuleUser.scenario
        activityScenario.onActivity { activity ->
            Activity = activity
        }

        Activity.readData(uid)

        var db = FirebaseDatabase.getInstance().getReference("Doctors")
        db.child(docUid).get().addOnSuccessListener {
            if(it.exists()) {
                rating = it.child("name").value.toString()
            }
        }.addOnFailureListener{

        }
        if(!rating.isNullOrEmpty())
            onView(withId(R.id.tvTotalRatingDocProfileUserView)).check(matches(withText("${rating}/5")))
    }


    @After
    fun tearDown() {
        activityScenario.close()
    }

}
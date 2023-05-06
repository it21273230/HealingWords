import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.*
import androidx.test.espresso.intent.matcher.IntentMatchers.*
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.hamcrest.Matchers.*
import org.junit.*
import org.junit.runner.RunWith

import org.junit.Rule
import org.junit.Test
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.example.healingwords.*


@RunWith(AndroidJUnit4::class)
class EditDocProfileTest {

    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(EditDocProfile::class.java)

    // Add this rule to enable intent verification
    @get:Rule
    var intentsTestRule = IntentsTestRule(EditDocProfile::class.java)

    private lateinit var activityScenario: ActivityScenario<EditDocProfile>
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    @Before
    fun setUp() {
        activityScenario = activityScenarioRule.scenario
        // Initialize Firebase and database references

    }

    @Test
    fun testEditDocProfile() {
        // Perform login

        onView(withId(R.id.btnEditDocProfileConfirm)).perform(click())


        // Wait for the login to complete and the MainActivity to start
        intending(hasComponent(DocMainUI::class.java.name)).respondWith(
            Instrumentation.ActivityResult(Activity.RESULT_OK, null)
        )

        onView(withId(R.id.docBottomNav)).check(matches(isDisplayed()))
    }



    @After
    fun tearDown() {
        activityScenario.close()
    }
}


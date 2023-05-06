import android.app.Activity
import android.app.Instrumentation
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.*
import androidx.test.espresso.intent.matcher.IntentMatchers.*
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.healingwords.LoginPage
import com.example.healingwords.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.hamcrest.Matchers.*
import org.junit.*
import org.junit.runner.RunWith
import com.example.healingwords.R

import org.junit.Rule
import org.junit.Test
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed


@RunWith(AndroidJUnit4::class)
class LoginPageTest {

    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(LoginPage::class.java)

    // Add this rule to enable intent verification
    @get:Rule
    var intentsTestRule = IntentsTestRule(LoginPage::class.java)

    private lateinit var activityScenario: ActivityScenario<LoginPage>
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    @Before
    fun setUp() {
        activityScenario = activityScenarioRule.scenario
        // Initialize Firebase and database references
        firebaseAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference
    }

    @Test
    fun testLogin() {
        // Type in valid email and password
        onView(withId(R.id.editEmailLogin)).perform(typeText("user@gmail.com"))
        onView(withId(R.id.editPasswordLogin)).perform(typeText("Qwerty@123"), closeSoftKeyboard())

        // Click on login button
        onView(withId(R.id.submitLoginBtn)).perform(click())

        Thread.sleep(100000)
        // Verify that the correct activity is launched based on the user's account type
        val currentFirebaseUser = FirebaseAuth.getInstance().currentUser
        if (currentFirebaseUser != null) {
            var doctorDatabase = FirebaseDatabase.getInstance().getReference("Doctors")
            doctorDatabase.child(currentFirebaseUser.uid).get().addOnSuccessListener { Doctor ->
                if (Doctor.exists()) {
                    onView(withId(R.id.doc_main_activity_layout)).check(matches(isDisplayed()))
                } else {
                    onView(withId(R.id.main_activity_layout)).check(matches(isDisplayed()))
                }
            }
        }
    }



    @After
    fun tearDown() {
        activityScenario.close()
    }
}


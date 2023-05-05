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
import com.example.healingwords.UserViewDocProfile
import org.junit.*
import com.example.healingwords.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import junit.framework.TestCase.assertEquals
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class TestUserViewDocProfile {
   private lateinit var auth: FirebaseAuth
   private lateinit var uid:String
   private lateinit var docUid:String
   private var name:String ? = null
   private lateinit var Activity : UserViewDocProfile

   private lateinit var appContext: Context
   @Test
   fun useAppContext() {
      // Context of the app under test.
      appContext = InstrumentationRegistry.getInstrumentation().targetContext
      assertEquals("com.example.healingwords", appContext.packageName)
   }

   @get:Rule
   val activityScenarioRule = ActivityScenarioRule(UserViewDocProfile::class.java)
   private lateinit var activityScenario: ActivityScenario<UserViewDocProfile>



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
   fun readData() {
      getUid()
      activityScenario = activityScenarioRule.scenario
      activityScenario.onActivity { activity ->
         Activity = activity
      }
      docUid = Activity.intent.getStringExtra("uid").toString()
      Activity.readData(docUid)
   }

   @Test
   fun testName(){
      activityScenario = activityScenarioRule.scenario
      activityScenario.onActivity { activity ->
         Activity = activity
      }
      docUid = Activity.intent.getStringExtra("uid").toString()
      Activity.readData(docUid)
      docUid = Activity.intent.getStringExtra("uid").toString()
      var db = FirebaseDatabase.getInstance().getReference("Doctors")
      db.child(docUid).get().addOnSuccessListener {
         if(it.exists()) {
            name = it.child("name").value.toString()
         }
      }.addOnFailureListener{

      }
     if(!name.isNullOrEmpty())
        onView(withId(R.id.tvDocNameUserView)).check(matches(withText(name)))
   }

   @After
   fun tearDown() {
      activityScenario.close()
   }

}
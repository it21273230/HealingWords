package com.example.healingwords

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import com.example.healingwords.databinding.ActivityShowReviewsBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class ShowReviews : AppCompatActivity() {
    private lateinit var allReviews: TextView
    private lateinit var myReviews: TextView
    private lateinit var binding: ActivityShowReviewsBinding
    private lateinit var fragContainer: FragmentContainerView

    private lateinit var fabAddReviews: FloatingActionButton
    private lateinit var docUid: String
    private lateinit var userUid: String
    private lateinit var mAuth: FirebaseAuth

    private lateinit var docName: String
    private lateinit var username: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowReviewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        allReviews = binding.fragSelectReviewAll
        myReviews = binding.fragSelectReviewMy
        fragContainer = binding.fragmentContainerView

        mAuth = FirebaseAuth.getInstance()
        fabAddReviews = findViewById(R.id.fabAddReviewBtn)
        docUid = intent.getStringExtra("docUid")!!
        val currentUser = FirebaseAuth.getInstance().currentUser
        userUid = currentUser!!.uid

        FirebaseDatabase.getInstance().getReference("Doctors").child(docUid).get().addOnSuccessListener { doc ->
            if(doc.exists()) {
                docName = doc.child("name").value.toString()
                FirebaseDatabase.getInstance().getReference("Users").child(userUid).get().addOnSuccessListener { user ->
                    if(user.exists()) {
                        username = user.child("username").value.toString()
                    } else {
                        username = "Anonymous"
                    }
                }
            } else {
                docName = ""
            }

        }

        val fragmentAllReviews = ShowAllReviews(docSpecified = true, docUid = docUid)
        val fragmentMyReviews = ShowAllReviews(editable = true, userAndDocSpecified = true, docUid = docUid, userUid = userUid )

        replaceFragment(fragmentAllReviews)

        fabAddReviews.setOnClickListener {
            val intent = Intent(this, AddReview::class.java)
            intent.putExtra("docUid", docUid)
            intent.putExtra("userUid", userUid)
            intent.putExtra("docName", docName)
            intent.putExtra("username", username)
            startActivity(intent)
            finish()
        }

        allReviews.setOnClickListener{
            allReviews.setTextColor(Color.parseColor("#029851"))
            myReviews.setTextColor(Color.parseColor("#000000"))
            showFAB(true)
            replaceFragment(fragmentAllReviews)
        }

        myReviews.setOnClickListener{
            allReviews.setTextColor(Color.parseColor("#000000"))
            myReviews.setTextColor(Color.parseColor("#029851"))
            showFAB(false)
            replaceFragment(fragmentMyReviews)
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(fragContainer.id, fragment)
            .commit()
    }

    private fun showFAB(state: Boolean) {
        if(state) {
            fabAddReviews.visibility = VISIBLE
            fabAddReviews.isClickable = true
        } else {
            fabAddReviews.visibility = INVISIBLE
            fabAddReviews.isClickable = false
        }
    }
}
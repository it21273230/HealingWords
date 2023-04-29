package com.example.healingwords

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import com.example.healingwords.databinding.ActivityShowReviewsBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth


class ShowReviews : AppCompatActivity() {
    private lateinit var allReviews: TextView
    private lateinit var myReviews: TextView
    private lateinit var binding: ActivityShowReviewsBinding
    private lateinit var fragContainer: FragmentContainerView

    private lateinit var fabAddReviews: FloatingActionButton
    private lateinit var docUid: String
    private lateinit var userUid: String
    private lateinit var mAuth: FirebaseAuth


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

        val fragmentAllReviews = ShowAllReviews(docSpecified = true, docUid = docUid)
        val fragmentMyReviews = ShowAllReviews(editable = true, userAndDocSpecified = true, docUid = docUid, userUid = userUid )

        replaceFragment(fragmentAllReviews)

        fabAddReviews.setOnClickListener {
            val intent = Intent(this, AddReview::class.java)
            intent.putExtra("docUid", docUid)
            intent.putExtra("userUid", userUid)
            startActivity(intent)
            finish()
        }

        allReviews.setOnClickListener{
            allReviews.setTextColor(Color.parseColor("#029851"))
            myReviews.setTextColor(Color.parseColor("#000000"))
            replaceFragment(fragmentAllReviews)
        }

        myReviews.setOnClickListener{
            allReviews.setTextColor(Color.parseColor("#000000"))
            myReviews.setTextColor(Color.parseColor("#029851"))
            replaceFragment(fragmentMyReviews)
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(fragContainer.id, fragment)
            .commit()
    }
}
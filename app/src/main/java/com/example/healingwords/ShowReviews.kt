package com.example.healingwords

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import com.example.healingwords.databinding.ActivityShowReviewsBinding


class ShowReviews : AppCompatActivity() {
    private lateinit var allReviews: TextView
    private lateinit var myReviews: TextView
    private lateinit var binding: ActivityShowReviewsBinding
    private lateinit var fragContainer: FragmentContainerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowReviewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        allReviews = binding.fragSelectReviewAll
        myReviews = binding.fragSelectReviewMy
        fragContainer = binding.fragmentContainerView

        val fragmentAllReviews = ShowAllReviews()
        val fragmentMyReviews = ShowAllReviews()

        replaceFragment(fragmentAllReviews)

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
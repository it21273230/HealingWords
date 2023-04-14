package com.example.healingwords

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.android.gms.common.SignInButton.ButtonSize


class DocProfile : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_doc_profile, container, false)

        val btnBlogs: Button = view.findViewById(R.id.btnViewBlogs)
        val btnReview: Button = view.findViewById<Button>(R.id.btnViewReviews)

        return view
    }

    fun btnReviewClicked(context: Context) {
        val intent = Intent(context, ShowReviews::class.java)
        startActivity(intent)
    }
}
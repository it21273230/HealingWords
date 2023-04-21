package com.example.healingwords

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.healingwords.databinding.FragmentDocProfileBinding
import com.google.android.gms.common.SignInButton.ButtonSize


class DocProfile : Fragment() {
    lateinit var btnReviews: Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_doc_profile, container, false)
        btnReviews = view.findViewById(R.id.btnViewReviews)

        btnReviews.setOnClickListener {
            val intent = Intent(activity, ShowAllReviewsActivity::class.java)
            startActivity(intent)
        }

        return view
    }




}
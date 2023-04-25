package com.example.healingwords

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.healingwords.databinding.FragmentDocProfileBinding
import com.google.android.gms.common.SignInButton.ButtonSize
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class DocProfile : Fragment() {

    lateinit var btnReviews: Button
    private lateinit var mAuth: FirebaseAuth
    private lateinit var tvName: TextView
    private lateinit var tvRating: TextView
    private lateinit var tvBio: TextView
    private lateinit var tvTitle: TextView
    private lateinit var uid: String
    private lateinit var database : DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mAuth = FirebaseAuth.getInstance()
        val currentFirebaseUser = FirebaseAuth.getInstance().currentUser

        val view =  inflater.inflate(R.layout.fragment_doc_profile, container, false)
        btnReviews = view.findViewById(R.id.btnViewReviews)

        btnReviews.setOnClickListener {
            val intent = Intent(activity, ShowAllReviewsActivity::class.java)
            startActivity(intent)
        }

        tvName = view.findViewById(R.id.tvDocName)
        tvBio = view.findViewById(R.id.tvDocBio)
        tvRating = view.findViewById(R.id.tvTotalRatingDocProfile)
        tvTitle = view.findViewById(R.id.tvDocTitle)

        uid = currentFirebaseUser!!.uid

        if(uid.isNotEmpty()) {
            readData(uid)
        }

        return view
    }

    private fun readData(uid: String) {
        database = FirebaseDatabase.getInstance().getReference("Doctors")
        database.child(uid).get().addOnSuccessListener {
            if(it.exists()) {
                var name = it.child("name").value
                var title = it.child("title").value
                var bio = it.child("bio").value
                var rating = it.child("rating").value

                tvName.text = name.toString()
                tvBio.text = bio.toString()
                tvRating.text = "${rating.toString()}/10"
                tvTitle.text = title.toString()

            }else {
                Toast.makeText(requireActivity(), "User doesn't exists", Toast.LENGTH_LONG).show()
            }
        }.addOnFailureListener{
            Toast.makeText(requireActivity(), "Failed", Toast.LENGTH_LONG).show()
        }
    }



}
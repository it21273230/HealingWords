package com.example.healingwords

import android.annotation.SuppressLint
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
import androidx.appcompat.app.AlertDialog
import com.example.healingwords.databinding.FragmentDocProfileBinding
import com.example.healingwords.models.Review
import com.google.android.gms.common.SignInButton.ButtonSize
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlin.math.round


class DocProfile : Fragment() {

    
    private lateinit var mAuth: FirebaseAuth
    private lateinit var tvName: TextView
    private lateinit var tvRating: TextView
    private lateinit var tvBio: TextView
    private lateinit var tvTitle: TextView
    private lateinit var btnEdit: Button
    private lateinit var btnDelete: Button
    private lateinit var uid: String
    private lateinit var tvNoOfReviews: TextView
    private lateinit var database : DatabaseReference
    private lateinit var reviewDbRef: DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mAuth = FirebaseAuth.getInstance()
        val currentFirebaseUser = FirebaseAuth.getInstance().currentUser

        val view =  inflater.inflate(R.layout.fragment_doc_profile, container, false)


        tvName = view.findViewById(R.id.tvDocName)
        tvBio = view.findViewById(R.id.tvDocBio)
        tvRating = view.findViewById(R.id.tvTotalRatingDocProfile)
        tvTitle = view.findViewById(R.id.tvDocTitle)
        tvNoOfReviews = view.findViewById(R.id.docProfileNoOfReviews)
        btnDelete = view.findViewById(R.id.btnDeleteDocProfile)
        btnEdit = view.findViewById(R.id.btnEditDocProfile)

        //edit profile
        btnEdit.setOnClickListener {
            val intent =Intent(requireActivity(), EditDocProfile::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        //delete profile
        btnDelete.setOnClickListener {
            val builder = AlertDialog.Builder(requireActivity())
            builder.setTitle("Confirm Delete")
            builder.setMessage("Do you want to delete this item?")

            builder.setPositiveButton("Delete") { _, _ ->
                var docUid = currentFirebaseUser!!.uid
                FirebaseDatabase.getInstance().getReference("Doctors").child(docUid).removeValue().addOnSuccessListener{
                    Toast.makeText(requireActivity(),"Deleted Successfully",Toast.LENGTH_LONG )
                    var intent = Intent(requireActivity(), LoginPage::class.java)
                    FirebaseAuth.getInstance().currentUser?.delete()
                    startActivity(intent)
                    requireActivity().finish()
                }
            }

            builder.setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }

            builder.create().show()


        }

        uid = currentFirebaseUser!!.uid

        if(uid.isNotEmpty()) {
            readData(uid)
            setTotalRating(uid)
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

                tvName.text = name.toString()
                tvBio.text = bio.toString()
                tvTitle.text = title.toString()
                btnDelete.text = "Delete"
                btnEdit.text = "Edit"

            }else {
                Toast.makeText(requireActivity(), "User doesn't exists", Toast.LENGTH_LONG).show()
            }
        }.addOnFailureListener{
            Toast.makeText(requireActivity(), "Failed", Toast.LENGTH_LONG).show()
        }
    }


    private fun setTotalRating(docUid: String) {
        var totStars = 0.0
        var totGivenStars = 0.0
        var noOfReviews = 0.0
        reviewDbRef =  FirebaseDatabase.getInstance().getReference("Reviews")
        reviewDbRef.addValueEventListener(object: ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(snapshot: DataSnapshot){
                if(snapshot.exists()) {
                    for(reviewSnapshot in snapshot.children) {
                        val review = reviewSnapshot.getValue(Review::class.java)
                        if(review!!.docUid == docUid) {
                            noOfReviews++
                            totStars += 5
                            totGivenStars += review.noOfStars!!.toInt()

                        }

                    }

                    val finalRating=((totGivenStars / (5*noOfReviews) )* 5)
                    tvRating.text = "$finalRating/5"
                    if(noOfReviews.toInt() == 0){
                        tvNoOfReviews.text = "( ${noOfReviews.toInt()} Review )"
                    } else {
                        tvNoOfReviews.text = "( ${noOfReviews.toInt()} Reviews )"
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


    }


}
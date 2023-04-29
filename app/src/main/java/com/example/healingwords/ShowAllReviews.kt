package com.example.healingwords

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healingwords.adapters.ReviewListAdapter
import com.example.healingwords.models.Review
import com.google.firebase.database.*


class ShowAllReviews(private var editable: Boolean = false, var docSpecified: Boolean = false, var userSpecified: Boolean = false, var userAndDocSpecified: Boolean = false, var userUid: String? = null, var docUid: String? = null) : Fragment() {
    private lateinit var reviewListRecyclerView: RecyclerView
    private lateinit var reviewList: ArrayList<Review>
    private lateinit var dbRef: DatabaseReference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_show_all_reviews, container, false)

        reviewListRecyclerView = view.findViewById(R.id.rvShowReviews)
        reviewListRecyclerView.layoutManager= LinearLayoutManager(requireActivity())
        reviewListRecyclerView.setHasFixedSize(true)

        reviewList = arrayListOf()
        reviewListRecyclerView.adapter = ReviewListAdapter(reviewList, editable)

        getReviews()

        return view
    }


    private fun getReviews() {
        dbRef = FirebaseDatabase.getInstance().getReference("Reviews")

        dbRef.addValueEventListener(object:ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot){
                if(snapshot.exists()) {
                    for(reviewSnapshot in snapshot.children) {
                        val review = reviewSnapshot.getValue(Review::class.java)
                        if(docSpecified) {
                            if(review!!.docUid == docUid) {
                                reviewList.add(review)
                            }
                        } else if(userAndDocSpecified) {
                            if(review!!.userUid == userUid && review.docUid == docUid) {
                                reviewList.add(review)
                            }
                        } else if(userSpecified){
                            if(review!!.userUid == userUid) {
                                reviewList.add(review)

                            }
                        } else {
                            reviewList.add(review!!)
                        }
                    }
                    val adapter = ReviewListAdapter(reviewList)
                    reviewListRecyclerView.adapter= adapter
                    adapter.setOnItemClickListener(object : ReviewListAdapter.OnItemClickListener {
                        override fun onItemClick(position: Int) {

                        }

                    })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

}
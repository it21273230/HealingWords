package com.example.healingwords

import Post
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class AccountFragment : Fragment() {

    private lateinit var postListView: RecyclerView
    private var postList: MutableList<Post> = mutableListOf()


    private lateinit var firebaseFirestore: FirebaseFirestore

    private lateinit var postRecyclerAdapterUser: PostRecyclerAdapterUser


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_account, container, false)

        postListView = view.findViewById(R.id.postListViewUser)
        postRecyclerAdapterUser = PostRecyclerAdapterUser(postList)
        postListView.layoutManager = LinearLayoutManager(requireActivity())

        postListView.adapter = postRecyclerAdapterUser


        firebaseFirestore = FirebaseFirestore.getInstance()


        val firstQuery = firebaseFirestore.collection("Posts")
            .orderBy("timestamp", Query.Direction.DESCENDING)


        firstQuery.addSnapshotListener { value, error ->
            if (error != null) {
                // Handle error
                return@addSnapshotListener
            }

           

            for (doc in value!!.documentChanges) {
                val postId = doc.document.id
                val post = doc.document.toObject(Post::class.java).withId<Post>(postId)
                post.timestamp = post.getTimestampAsLong()

                when (doc.type) {
                    DocumentChange.Type.ADDED -> {
                        
                        postList.add(post)
                        postRecyclerAdapterUser.notifyDataSetChanged()
                    }
                    else -> {
                        //handle something
                    }
                }
            }

            postListView.adapter?.notifyDataSetChanged()
        }



        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        postList.clear()
    }



}
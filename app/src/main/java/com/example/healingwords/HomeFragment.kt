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
import java.sql.Timestamp


class HomeFragment : Fragment() {

    private lateinit var postListView: RecyclerView
    private var postList: MutableList<Post> = mutableListOf()

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseFirestore: FirebaseFirestore

    private lateinit var postRecyclerAdapter: PostRecyclerAdapter
    private lateinit var  addPostBtn: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        postListView = view.findViewById(R.id.post_list_view)
        postRecyclerAdapter = PostRecyclerAdapter(postList)
        postListView.layoutManager = LinearLayoutManager(requireActivity())

        postListView.adapter = postRecyclerAdapter


        firebaseFirestore = FirebaseFirestore.getInstance()

        addPostBtn = view.findViewById(R.id.addPostFloatingBtn)
        addPostBtn.setOnClickListener {
            val newpostIntent = Intent(requireActivity(), NewPostActivity::class.java)
            startActivity(newpostIntent)
        }

        val firstQuery = firebaseFirestore.collection("Posts")
            .orderBy("timestamp", Query.Direction.DESCENDING)


        firstQuery.addSnapshotListener { value, error ->
            if (error != null) {
                // Handle error
                return@addSnapshotListener
            }

            postList.clear() // clear post

            for (doc in value!!.documentChanges) {

                val postId = doc.document.id
                val post = doc.document.toObject(Post::class.java).withId<Post>(postId)
                post.timestamp = post.getTimestampAsLong()

                when (doc.type) {
                    DocumentChange.Type.ADDED -> {
                        postList.add(post)
                        postRecyclerAdapter.notifyDataSetChanged()
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
}

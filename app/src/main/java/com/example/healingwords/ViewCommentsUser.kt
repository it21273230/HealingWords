package com.example.healingwords

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healingwords.adapters.CommentAdapter
import com.example.healingwords.adapters.CommentAdapterUser
import com.google.firebase.database.*

class ViewCommentsUser : AppCompatActivity() {

    private lateinit var dbref: DatabaseReference
    private lateinit var CommentRecyclerView: RecyclerView
    private lateinit var commentArrayList: ArrayList<Comment>
    private lateinit var postId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_comments_user)

        postId = intent.getStringExtra("postId") ?: ""
        CommentRecyclerView = findViewById(R.id.commentListUser)
        CommentRecyclerView.layoutManager = LinearLayoutManager(this)
        CommentRecyclerView.setHasFixedSize(true)

        commentArrayList = arrayListOf<Comment>()
        getCommentData()
    }

    private fun getCommentData() {

        dbref = FirebaseDatabase.getInstance().getReference("comments")
        val comments : Query = dbref.orderByChild("postId").equalTo(postId)
        comments.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()){
                    for (commentSnapshot in snapshot.children){

                        val comment = commentSnapshot.getValue(Comment::class.java)
                        commentArrayList.add(comment!!)

                    }
                    CommentRecyclerView.adapter = CommentAdapterUser(commentArrayList)

                }

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}
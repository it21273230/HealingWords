package com.example.healingwords

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healingwords.adapters.CommentAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*

class ViewComments : AppCompatActivity() {

    private lateinit var postId: String
    private lateinit var userId: String
    private lateinit var dbref: DatabaseReference
    private lateinit var CommentRecyclerView: RecyclerView
    private lateinit var commentArrayList: ArrayList<Comment>

    private lateinit var AddCommentbtn: FloatingActionButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_comments)

        postId = intent.getStringExtra("postId") ?: ""
        userId = intent.getStringExtra("userId") ?: ""

        AddCommentbtn = findViewById(R.id.AddCommentbtn)

        AddCommentbtn.setOnClickListener{
            Log.d("postId", postId)

            val intent = Intent(this, AddComment::class.java)
            intent.putExtra("postId", postId)
            intent.putExtra("userId", userId)
            startActivity(intent)
        }

        CommentRecyclerView = findViewById(R.id.commentList)
        CommentRecyclerView.layoutManager = LinearLayoutManager(this)
        CommentRecyclerView.setHasFixedSize(true)

        commentArrayList = arrayListOf<Comment>()
        getCommentData()


    }

    private fun getCommentData() {

        dbref = FirebaseDatabase.getInstance().getReference("comments")
        val comments : Query = dbref.orderByChild("postId").equalTo(postId)
        comments.addValueEventListener(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()){
                    for (commentSnapshot in snapshot.children){

                        val comment = commentSnapshot.getValue(Comment::class.java)
                        commentArrayList.add(comment!!)

                    }
                    CommentRecyclerView.adapter = CommentAdapter(commentArrayList)

                }

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}
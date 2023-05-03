package com.example.healingwords

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healingwords.adapters.CommentAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ViewComments : AppCompatActivity() {

    private lateinit var postId: String
    private lateinit var userId: String
    private lateinit var dbref: DatabaseReference
    private lateinit var CommentRecyclerView: RecyclerView
    private lateinit var commentArrayList: ArrayList<Comment>
    private lateinit var ToolBar : Toolbar
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var AddCommentbtn: FloatingActionButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_comments)

        ToolBar = findViewById(R.id.viewCommentToolbar)
        setSupportActionBar(ToolBar)
        supportActionBar?.setTitle("Comments")

        postId = intent.getStringExtra("postId") ?: ""
        userId = intent.getStringExtra("userId") ?: ""
        dbref = FirebaseDatabase.getInstance().reference
        firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.uid

        if (currentUser != null) {
            dbref.child("Users").child(currentUser).get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val dataSnapshot = task.result
                    if(dataSnapshot.exists()){
                        AddCommentbtn.visibility = View.INVISIBLE
                    }
                }
            }
        }

        AddCommentbtn = findViewById(R.id.AddCommentbtn)

        AddCommentbtn.setOnClickListener{
            Log.d("postId", postId)

            val intent = Intent(this, AddComment::class.java)
            intent.putExtra("postId", postId)
            intent.putExtra("userId", userId)
            startActivity(intent)
            finish()
        }

        CommentRecyclerView = findViewById(R.id.commentListUser)
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
                    CommentRecyclerView.adapter = CommentAdapter(postId, commentArrayList)

                }

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}
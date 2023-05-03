package com.example.healingwords


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healingwords.models.Blog
import com.google.firebase.database.*


class AllBlogs : AppCompatActivity() {

    private lateinit var dbRef : DatabaseReference
    private lateinit var blogRecyclerView: RecyclerView
    private lateinit var blogArrayList: ArrayList<Blog>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_blog)

        blogRecyclerView = findViewById(R.id.blogList)
        blogRecyclerView.layoutManager = LinearLayoutManager(this)
        blogRecyclerView.setHasFixedSize(true)

        blogArrayList = arrayListOf<Blog>()
        getBlogData()


    }

    private fun getBlogData() {

        dbRef = FirebaseDatabase.getInstance().getReference("Blogs")

        dbRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                if(snapshot.exists()){
                    for (userSnapshot in snapshot.children){

                        val blog = userSnapshot.getValue(Blog::class.java)
                        blogArrayList.add(blog!!)

                    }

                    blogRecyclerView.adapter = BlogAdaptor(blogArrayList)
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

    }
}
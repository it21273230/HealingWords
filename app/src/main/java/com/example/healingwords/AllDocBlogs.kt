package com.example.healingwords

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.healingwords.adapters.UpdateBlogsAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healingwords.databinding.ActivityAllDocBlogsBinding
import com.example.healingwords.models.Blog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class AllDocBlogs : AppCompatActivity(), UpdateBlogsAdapter.UpdateBlogAdapterClicksInterface {

    private lateinit var btnAddBlog : FloatingActionButton
    private lateinit var docId : String
    private lateinit var dbRef : DatabaseReference
    private lateinit var blogRecyclerView: RecyclerView
    private lateinit var mAuth: FirebaseAuth
    private lateinit var blogArrayList: ArrayList<Blog>
    private lateinit var binding: ActivityAllDocBlogsBinding
    private lateinit var adapter:UpdateBlogsAdapter
    private lateinit var BlogList:ArrayList<Blog>
    private lateinit var database : DatabaseReference
    private lateinit var ID :String
    private lateinit var docName : String





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllDocBlogsBinding.inflate(layoutInflater)
        setContentView(binding.root)



       btnAddBlog = findViewById(R.id.AddBlog)

        btnAddBlog.setOnClickListener {
            val intent = Intent(this,AddBlog::class.java)
            startActivity(intent)
            finish()


        }

        //blogRecyclerView = findViewById(R.id.docBlogList)
        //blogRecyclerView.layoutManager = LinearLayoutManager(this)
        //blogRecyclerView.setHasFixedSize(true)

        //blogArrayList = arrayListOf<Blog>()


        binding.docBlogList.setHasFixedSize(true)
        binding.docBlogList.layoutManager = LinearLayoutManager(this)
        BlogList = arrayListOf()
        adapter = UpdateBlogsAdapter(BlogList)

        binding.docBlogList.adapter = adapter



        getBlogData()

    }

    private fun getBlogData() {

        dbRef = FirebaseDatabase.getInstance().getReference("Blogs")

        mAuth = FirebaseAuth.getInstance()
        val currentFirebaseUser = FirebaseAuth.getInstance().currentUser

        docId = currentFirebaseUser!!.uid

        val blogs : Query = dbRef.orderByChild("docId").equalTo(docId)



        blogs.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                BlogList.clear()
                if(snapshot.exists()){
                for(taskSnapshot in snapshot.children){



                            val blog = taskSnapshot.getValue(Blog::class.java)
                            BlogList.add(blog!!)



                        //blogRecyclerView.adapter = BlogAdaptor(blogArrayList)
                        binding.docBlogList.adapter = UpdateBlogsAdapter(BlogList)
                    }
                }



            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

    }

    override fun onDeleteTaskButton(blog: Blog) {

        blog.blogId?.let {
            dbRef.child(it).removeValue().addOnCompleteListener {
                if(it.isSuccessful) {
                    Toast.makeText(this,"Deleted",Toast.LENGTH_SHORT).show()
                } else{
                    Toast.makeText(this,"not Deleted",Toast.LENGTH_SHORT).show()

                }


            }
        }

    }

    override fun onEditTaskButton(blog: Blog) {
        TODO("Not yet implemented")
    }
}
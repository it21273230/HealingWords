package com.example.healingwords

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.healingwords.databinding.ActivityEditBlogBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class EditBlog : AppCompatActivity() {

    private lateinit var binding: ActivityEditBlogBinding
    private lateinit var database: DatabaseReference
    private lateinit var blogid : String
    private lateinit var docId : String
    private lateinit var title : EditText
    private lateinit var desc : EditText
    private lateinit var submitEditBtn : Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //binding = ActivityEditBlogBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_edit_blog)

        database = FirebaseDatabase.getInstance().reference

        title = findViewById(R.id.editBlogTitle)
        desc = findViewById(R.id.editBlogDesc)
        submitEditBtn = findViewById(R.id.submitEditBtn)

        blogid = intent.getStringExtra("blogId")?:""

        database.child("Blogs").child(blogid).get().addOnCompleteListener { task ->
            if (task.isSuccessful){
                val dataSnapshot = task.result
                if (dataSnapshot.exists())
                {
                    val blogTitle = dataSnapshot.child("title").value.toString()
                    val blogDesc = dataSnapshot.child("desc").value.toString()

                    title.setText(blogTitle)
                    desc.setText(blogDesc)

                }
            }
        }

        submitEditBtn.setOnClickListener {


            val title = title.text.toString()
            val des = desc.text.toString()

            updateBlog(title,des)


        }





    }

    private fun updateBlog(title:String,des:String){
        Log.d("title",title)




        val blog = mapOf<String,String>(
            "title" to title,
            "desc" to des
        )

        database.child("Blogs").child(blogid).updateChildren(blog).addOnSuccessListener {


            Toast.makeText(this,"Updated",Toast.LENGTH_SHORT).show()
        }.addOnFailureListener{
            Toast.makeText(this,"Not Updated",Toast.LENGTH_SHORT).show()
        }

    }

}
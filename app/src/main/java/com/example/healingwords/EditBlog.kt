package com.example.healingwords

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.healingwords.databinding.ActivityEditBlogBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class EditBlog : AppCompatActivity() {

    private lateinit var binding: ActivityEditBlogBinding
    private lateinit var database: DatabaseReference
    private lateinit var blogid : String
    private lateinit var docId : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBlogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.submitEditBtn.setOnClickListener {


            val title = binding.editBlogTitle.text.toString()
            val des = binding.editBlogDesc.text.toString()

            updateBlog(title,des)


        }





    }

    private fun updateBlog(title:String,des:String){
        database = FirebaseDatabase.getInstance().getReference("Blogs")



        val blog = mapOf<String,String>(
            "title" to title,
            "desc" to des
        )

        database.child(title).updateChildren(blog).addOnSuccessListener {

            binding.editBlogTitle.text.clear()
            binding.editBlogDesc.text.clear()
            Toast.makeText(this,"Updated",Toast.LENGTH_SHORT).show()
        }.addOnFailureListener{
            Toast.makeText(this,"Not Updated",Toast.LENGTH_SHORT).show()
        }

    }

}
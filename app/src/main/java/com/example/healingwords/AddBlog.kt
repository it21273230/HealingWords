package com.example.healingwords

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.healingwords.databinding.ActivityAddABlogBinding
import com.example.healingwords.models.Blog
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.UUID

class AddBlog : AppCompatActivity() {
    private lateinit var docId : String
    private lateinit var title : EditText
    private lateinit var mAuth: FirebaseAuth
    private lateinit var desc : EditText
    private lateinit var binding : ActivityAddABlogBinding
    private lateinit var dbRef : DatabaseReference
    private lateinit var docName : String
    private lateinit var database : DatabaseReference
    private lateinit var btnSubmit: Button
    private lateinit var btnCancel: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddABlogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()
        val currentFirebaseUser = FirebaseAuth.getInstance().currentUser

        docId = currentFirebaseUser!!.uid

        database = FirebaseDatabase.getInstance().getReference("Doctors")

        database.child(docId).get().addOnSuccessListener {

                var name = it.child("name").value


                docName = name.toString()


        }

        desc = binding.newBlogDesc
        btnSubmit = binding.postBtn
        btnCancel = binding.cancelPostBtn
        title = binding.newBlogTitle

        btnSubmit.setOnClickListener {
            dbRef = FirebaseDatabase.getInstance().getReference("Blogs")
            var uniqueID = UUID.randomUUID().toString()
            if(desc.text.isNotEmpty() && title.text.isNotEmpty())
            {
                val blog = Blog(uniqueID,docId,title.text.toString(),desc.text.toString(),docName)
                dbRef.child(uniqueID).setValue(blog).addOnSuccessListener {
                    Toast.makeText(this,"Successfully Saved", Toast.LENGTH_LONG).show()

                    var intent = Intent(this, AllDocBlogs::class.java)
                    intent.putExtra("docId", docId)
                    startActivity(intent)
                    finish()


                }.addOnFailureListener {
                    Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show()
                }
            }else {
                Toast.makeText(this, "Please Fill in all the fields", Toast.LENGTH_LONG).show()
            }
        }
        btnCancel.setOnClickListener {
            var intent = Intent(this, ShowReviews::class.java)
            intent.putExtra("docId", docId)
            startActivity(intent)
            finish()
        }







    }
}
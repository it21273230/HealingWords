package com.example.healingwords

import android.content.Intent
import android.icu.text.CaseMap.Title
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.healingwords.databinding.ActivityEditDocProfileBinding
import com.example.healingwords.models.Doctor
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class EditDocProfile : AppCompatActivity() {
    private lateinit var binding: ActivityEditDocProfileBinding
    private lateinit var edtName: EditText
    private lateinit var edtEmail: EditText
    private lateinit var edtUsername: EditText
    private lateinit var edtTitle: EditText
    private lateinit var updatedDoc: Doctor
    private lateinit var dbRef: DatabaseReference
    private lateinit var btnSubmit: Button
    private lateinit var btnCancel:Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditDocProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        edtName = binding.edtbtnEditDocProfileName
        edtEmail = binding.edtbtnEditDocProfileEmail
        edtTitle = binding.edtbtnEditDocProfileTitle
        edtUsername = binding.edtbtnEditDocProfileUsername
        btnSubmit = binding.btnEditDocProfileConfirm
        btnCancel = binding.btnEditDocProfileCancel

       val uid: String =  FirebaseAuth.getInstance().currentUser?.uid.toString()

        btnSubmit.setOnClickListener {
            dbRef = FirebaseDatabase.getInstance().getReference("Doctors")
                dbRef.child(uid).get().addOnSuccessListener {
                if(it.exists()) {
                    var rating = it.child("rating")

                    updatedDoc = Doctor(uid=uid, name=edtName.text.toString(), email=edtEmail.text.toString(), title = edtTitle.text.toString(), username = edtUsername.text.toString(), bio="", rating = rating.toString())
                }
            }

            dbRef.child(uid).setValue(updatedDoc).addOnSuccessListener {
                Toast.makeText(applicationContext, "Updated Successfully", Toast.LENGTH_LONG)
                val intent = Intent(this, DocProfile::class.java)
                startActivity(intent)
                finish()
            }.addOnFailureListener{
                Toast.makeText(applicationContext, "Failed", Toast.LENGTH_LONG)
            }
        }

        btnCancel.setOnClickListener {
            val intent = Intent(this, DocProfile::class.java)
            startActivity(intent)
            finish()
        }


    }
}
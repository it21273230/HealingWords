package com.example.healingwords

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class ChooseRegType : AppCompatActivity() {
    lateinit var btnRegNormal : Button
    lateinit var btnRegDoctor : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_reg_type)

        btnRegDoctor = findViewById(R.id.btnDocReg)
        btnRegNormal = findViewById(R.id.btnNormalReg)

        btnRegNormal.setOnClickListener {
            val intent = Intent(this, RegisterPage::class.java)
            startActivity(intent)
            finish()
        }

        btnRegDoctor.setOnClickListener {
            val intent = Intent(this, RegisterDoctorPage::class.java)
            startActivity(intent)
            finish()
        }

    }
}
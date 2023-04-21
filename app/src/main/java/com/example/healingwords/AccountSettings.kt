package com.example.healingwords


import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import de.hdodenhof.circleimageview.CircleImageView;






class AccountSettings : AppCompatActivity() {

    private lateinit var ToolBar: Toolbar
    private lateinit var AccountImage: CircleImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_settings)

        ToolBar = findViewById(R.id.accountToolBar)
        setSupportActionBar(ToolBar)
        supportActionBar?.setTitle("Account Setup")

        AccountImage = findViewById(R.id.accountImage)

        AccountImage.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show()
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)


                } else {
                    //profile image select yet to be implemented
                }
            }
        }

    }
}
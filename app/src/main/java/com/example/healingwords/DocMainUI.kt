package com.example.healingwords

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import com.example.healingwords.databinding.ActivityDocMainUiBinding
import com.example.healingwords.databinding.ActivityRegisterDoctorPageBinding
import com.google.firebase.auth.FirebaseAuth

class DocMainUI : AppCompatActivity() {
    private lateinit var mainToolBar: Toolbar
    private lateinit var binding: ActivityDocMainUiBinding
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDocMainUiBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mAuth = FirebaseAuth.getInstance()

        mainToolBar = findViewById(R.id.mainToolBar)
        setSupportActionBar(mainToolBar)

        supportActionBar?.setTitle("Healing Words")

        //ui elements binding
        val imgHome = binding.imgHomeDoc
        val imgReviews = binding.imgReviewsDoc
        val imgBlogs = binding.imgBlogsDoc
        val imgProfile = binding.imgDoc

        //fragments
        val fragmentHome = TempHome()  // replace this with actual home frag
        val fragmentReviews = ShowAllReviews()
        val fragmentBlogs = ""  //// replace this with actual blogs frag
        val fragmentDocProfile = DocProfile()

        //navigation profile page
        imgProfile.setOnClickListener{
            imgHome.setImageResource(R.drawable.unselected_home)
            imgBlogs.setImageResource(R.drawable.unselected_blog)
            imgReviews.setImageResource(R.drawable.review_unselected)
            imgProfile.setImageResource(R.drawable.selected_user)

            supportFragmentManager.beginTransaction().apply {
                replace(binding.DocMainUIFragmentContainerView.id, fragmentDocProfile)
                commit()
            }
        }

        //navigation reviews page
        imgReviews.setOnClickListener{
            imgHome.setImageResource(R.drawable.unselected_home)
            imgBlogs.setImageResource(R.drawable.unselected_blog)
            imgReviews.setImageResource(R.drawable.review_selected)
            imgProfile.setImageResource(R.drawable.unselected_user)

            supportFragmentManager.beginTransaction().apply {
                replace(binding.DocMainUIFragmentContainerView.id, fragmentReviews)
                commit()
            }
        }

        //navigation home page
        imgHome.setOnClickListener{
            imgHome.setImageResource(R.drawable.selected_home)
            imgBlogs.setImageResource(R.drawable.unselected_blog)
            imgReviews.setImageResource(R.drawable.review_unselected)
            imgProfile.setImageResource(R.drawable.unselected_user)

            supportFragmentManager.beginTransaction().apply {
                replace(binding.DocMainUIFragmentContainerView.id, fragmentHome)
                commit()
            }
        }

    }

    override fun onStart() {
        super.onStart()

        val currentUser = FirebaseAuth.getInstance().currentUser;
        if(currentUser == null){
            sendToLogin()
        }
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.main_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.actionLogoutBtn -> {
                logOut()
            }
        }


        return true
    }

    private fun logOut() {

        mAuth.signOut()
        sendToLogin()

    }

    private fun sendToLogin() {
        val loginIntent = Intent(this, LoginPage::class.java)
        startActivity(loginIntent);
        finish();
    }
}
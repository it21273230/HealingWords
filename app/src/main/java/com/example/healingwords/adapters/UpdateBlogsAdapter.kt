package com.example.healingwords.adapters

import android.view.LayoutInflater
import android.view.View
import android.content.Context
import android.content.Intent
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.healingwords.EditBlog
import com.example.healingwords.R
import com.example.healingwords.databinding.EditBlogItemBinding
import com.example.healingwords.models.Blog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class UpdateBlogsAdapter (private val docblogs: ArrayList<Blog>) : RecyclerView.Adapter<UpdateBlogsAdapter.BlogUpdateHolder>() {


    private lateinit var dbref : DatabaseReference
    private lateinit var firebaseauth: FirebaseAuth




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlogUpdateHolder {
        val itemView = LayoutInflater.from(parent.context).inflate((R.layout.edit_blog_item),parent,false)


        dbref = FirebaseDatabase.getInstance().reference
        firebaseauth = FirebaseAuth.getInstance()

        return BlogUpdateHolder(itemView)


    }

    override fun getItemCount(): Int {

        return docblogs.size

    }

    override fun onBindViewHolder(holder: BlogUpdateHolder, position: Int) {

        val currentItem = docblogs[position]
        val userId = currentItem.docId.toString()
        val blogId = currentItem.blogId

        holder.blogTitle.text = currentItem.title
        holder.desc.text = currentItem.desc

        holder.btndlt.setOnClickListener {
            AlertDialog.Builder(holder.contexts)
                .setTitle("Delete Blog")
                .setMessage("Are you sure you want to delete this blog?")
                .setPositiveButton("Delete") { _, _ ->
                    blogId?.let { id ->
                        dbref.child("Blogs").child(id).removeValue().addOnSuccessListener {
                            Toast.makeText(holder.contexts, "Blog Deleted", Toast.LENGTH_LONG).show()
                        }
                    }
                }
                .setNegativeButton("Cancel", null)
                .show()

        }

        holder.btnedit.setOnClickListener {
            val intent = Intent(holder.itemView.context,EditBlog::class.java)
            intent.putExtra("blogId",blogId)
            holder.itemView.context.startActivity(intent)
        }



    }

    interface UpdateBlogAdapterClicksInterface{
        fun onDeleteTaskButton(blog: Blog)
        fun onEditTaskButton(blog: Blog)

    }

    class BlogUpdateHolder (itemView: View): RecyclerView.ViewHolder(itemView){

        val blogTitle : TextView = itemView.findViewById(R.id.blogtitle)
        val desc : TextView = itemView.findViewById(R.id.blogdesc)
        val btndlt : ImageView = itemView.findViewById(R.id.deleteBlog)
        val btnedit : ImageView = itemView.findViewById(R.id.editBlog)
        val contexts: Context = itemView.context


    }

}

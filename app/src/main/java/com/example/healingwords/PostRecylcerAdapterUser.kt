package com.example.healingwords

import Post
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class PostRecyclerAdapterUser(private val postList: List<Post>) :
    RecyclerView.Adapter<PostRecyclerAdapterUser.ViewHolder>() {

    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database : DatabaseReference

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val descView: TextView = itemView.findViewById(R.id.postDesc)
        private val dateView: TextView = itemView.findViewById(R.id.postDate)
        private val username: TextView = itemView.findViewById(R.id.postUserName)
        val editImg: ImageView = itemView.findViewById(R.id.postEditImg)
        val deleteImg: ImageView = itemView.findViewById(R.id.postDeleteImg)
        val contexts: Context = itemView.context
         var postLikeBtn: ImageView = itemView.findViewById(R.id.postLikeBtn)
         var postLikeCount: TextView = itemView.findViewById(R.id.postLikeCount)
        var commentBtn: ImageView = itemView.findViewById(R.id.commentBtn)
        var postCommentCount: TextView = itemView.findViewById(R.id.postCommentCount)

        fun setDescText(descText: String) {
            descView.text = descText
        }
        fun setDateText(dateText: Long) {
            val date = Date(dateText)
            val formattedDate = SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.getDefault()).format(date)
            dateView.text = formattedDate
        }
        fun setUserName(usernameText: String) {
            username.text = usernameText
        }

        fun updateLikesCount(count: Int) {
            postLikeCount = itemView.findViewById(R.id.postLikeCount)
            postLikeCount.text = "$count Likes"
        }
        fun updateCommentsCount(count: Int) {
            postCommentCount = itemView.findViewById(R.id.postCommentCount)
            postCommentCount.text = "$count Comments"
        }


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.post_list_item, parent, false)
        firebaseFirestore = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
        return ViewHolder(view)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = postList[position]
        val postId = post.postId
        val descData = post.desc
        val dateData = post.timestamp
        val userId = post.userId
        val currentUserId = firebaseAuth.currentUser?.uid

        if (userId != currentUserId) {
            // If the post doesn't belong to the current user, hide it
            holder.itemView.visibility = View.GONE
            holder.itemView.layoutParams = RecyclerView.LayoutParams(0, 0)
            return
        }

        holder.setDescText(descData)
        if (dateData != null) {
            holder.setDateText(dateData)
        }

//        holder.itemView.setOnClickListener {
//            val intent = Intent(holder.itemView.context, userPostUpdate::class.java)
//            intent.putExtra("postId", postId)
//            holder.itemView.context.startActivity(intent)
//        }

        holder.editImg.setOnClickListener {
            val intent = Intent(holder.itemView.context, userPostUpdate::class.java)
            intent.putExtra("postId", postId)
            holder.itemView.context.startActivity(intent)
        }

        holder.deleteImg.setOnClickListener {
            val builder = AlertDialog.Builder(holder.contexts)
            builder.setTitle("Delete Post")
            builder.setMessage("Are you sure you want to delete this post?")
            builder.setPositiveButton("Yes") { dialog, which ->
                // User clicked Yes button
                if (postId != null) {
                    firebaseFirestore.collection("Posts").document(postId)
                        .delete()
                        .addOnSuccessListener {
                            Toast.makeText(holder.contexts, "Post deleted successfully", Toast.LENGTH_SHORT).show()
                            val intent = Intent(holder.contexts, MainActivity::class.java)
                            intent.putExtra("openFragment", "account")
                            holder.contexts.startActivity(intent)
                            // Finish the current activity
                            if (holder.contexts is Activity) {
                                (holder.contexts as Activity).finish()
                            }
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(holder.contexts, "Error deleting post: $e", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            builder.setNegativeButton("No") { dialog, which ->
                // User clicked No button
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()

        }


        firebaseFirestore.collection("Users").document(userId)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    if (document != null) {
                        // Document exists, do something with it
                        val username = document.getString("name")
                        if (username != null) {
                            holder.setUserName(username)
                        }
                    } else {
                        // Document does not exist
                        // Handle the error or show an error message
                    }
                } else {
                    // Task failed with an exception
                    // Handle the error or show an error message
                    val exception = task.exception
                    // ...
                }
            }

        //comments count
        database.child("comments").orderByChild("postId").equalTo(postId).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val count = dataSnapshot.childrenCount.toInt()
                holder.updateCommentsCount(count)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                //
            }
        })

        firebaseFirestore.collection("Posts/$postId/Likes").addSnapshotListener { snapshot, error ->
            if (error != null) {
                // Handle any errors
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val count = snapshot.size()
                holder.updateLikesCount(count)
            } else {
                holder.updateLikesCount(0)
            }
        }

        //get likes
        if (currentUserId != null) {
            firebaseFirestore.collection("Posts/$postId/Likes").document(currentUserId)
                .addSnapshotListener { documentSnapshot, error ->
                    if (error != null) {
                        // Handle any errors
                        return@addSnapshotListener
                    }

                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        holder.postLikeBtn.setImageDrawable(holder.itemView.context.getDrawable(R.drawable.selected_like))
                    } else {
                        holder.postLikeBtn.setImageDrawable(holder.itemView.context.getDrawable(R.drawable.unselected_like))
                    }
                }
        }

        //likes feature
        holder.postLikeBtn.setOnClickListener {
            val likesMap = hashMapOf<String, Any>()
            likesMap["timestamp"] = FieldValue.serverTimestamp()

            if (currentUserId != null) {
                firebaseFirestore.collection("Posts/$postId/Likes")
                    .document(currentUserId)
                    .get()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val document = task.result
                            if (document != null && document.exists()) {
                                // User already liked this post, delete their like
                                firebaseFirestore.collection("Posts/$postId/Likes")
                                    .document(currentUserId)
                                    .delete()
                            } else {
                                // User hasn't liked this post, create a new like document
                                firebaseFirestore.collection("Posts/$postId/Likes")
                                    .document(currentUserId)
                                    .set(likesMap)
                            }
                        }
                    }
            }
        }

        holder.commentBtn.setOnClickListener {

            val intent = Intent(holder.itemView.context, ViewCommentsUser::class.java)
            intent.putExtra("postId", postId)
            intent.putExtra("userId", currentUserId)
            holder.itemView.context.startActivity(intent)
        }
    }


    override fun getItemCount(): Int {
        // Return number of items here
        return postList.size
    }
}

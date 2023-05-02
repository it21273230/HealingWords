package com.example.healingwords

import Post
import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class PostRecyclerAdapter(private val postList: MutableList<Post>) :
    RecyclerView.Adapter<PostRecyclerAdapter.ViewHolder>() {

    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val descView: TextView = itemView.findViewById(R.id.postDesc)
        private val dateView: TextView = itemView.findViewById(R.id.postDate)
        private val username: TextView = itemView.findViewById(R.id.postUserName)

         var postLikeBtn: ImageView = itemView.findViewById(R.id.postLikeBtn)
         var postLikeCount: TextView = itemView.findViewById(R.id.postLikeCount)
         var commentBtn: ImageView = itemView.findViewById(R.id.commentBtn)

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



    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.post_list_item, parent, false)
        firebaseFirestore = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()

        return ViewHolder(view)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var postId = postList.get(position).postId
        var descData = postList.get(position).desc
        var dateData = postList.get(position).timestamp
        var currentUserId = firebaseAuth.currentUser?.uid

        var userId = postList.get(position).userId
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


        holder.setDescText(descData)
        if (dateData != null) {
            holder.setDateText(dateData)
        }

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

            val intent = Intent(holder.itemView.context, ViewComments::class.java)
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

package com.example.healingwords.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.healingwords.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class CommentAdapterUser(private val commentList: ArrayList<Comment>) : RecyclerView.Adapter<CommentAdapterUser.MyViewHolder> (){

    private lateinit var userdb : DatabaseReference


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.comment_item, parent,false)

        userdb = FirebaseDatabase.getInstance().reference


        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentitem = commentList[position]
        val userId = currentitem.userId.toString()
        val commentId = currentitem.commentId

        userdb.child("Users").child(userId).get().addOnCompleteListener{ task ->
            if(task.isSuccessful){
                val dataSnapshot = task.result
                if(dataSnapshot.exists()){
                    val username = dataSnapshot.child("username").value.toString()
                    holder.userId.text = username
                }
            }
        }

        //holder.userId.text = username
        holder.commentBody.text = currentitem.commentBody

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, CommentUpdate::class.java)
            intent.putExtra("commentId", commentId)
            holder.itemView.context.startActivity(intent)
        }
    }



    override fun getItemCount(): Int {
        return commentList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val userId : TextView = itemView.findViewById(R.id.tvname)
        val commentBody : TextView = itemView.findViewById(R.id.tvcomment)

    }

}
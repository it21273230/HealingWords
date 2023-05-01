package com.example.healingwords.adapters

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.healingwords.EditReview
import com.example.healingwords.R
import com.example.healingwords.ShowReviews
import com.example.healingwords.models.Review
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ReviewListAdapter(private val reviewList: ArrayList<Review>, private val editable: Boolean = false) : RecyclerView.Adapter<ReviewListAdapter.ReviewListViewHolder>() {
    private lateinit var mListener: OnItemClickListener
    interface OnItemClickListener {
        fun onItemClick(position: Int )
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReviewListViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.show_review_item, parent, false)
        return ReviewListViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: ReviewListViewHolder, position: Int) {
       val currentItem = reviewList[position]

        if(!editable) {
            holder.editBtn.visibility = INVISIBLE
            holder.editBtn.isClickable = false
            holder.deleteBtn.isClickable = false
            holder.deleteBtn.visibility = INVISIBLE
        } else {
            holder.editBtn.visibility = VISIBLE
            holder.editBtn.isClickable = true
            holder.deleteBtn.isClickable = true
            holder.deleteBtn.visibility = VISIBLE
        }

        holder.rate.rating = currentItem.noOfStars!!.toFloat()
        holder.user.text = currentItem.username.toString()
        holder.doc.text = "For Dr. ${currentItem.docName.toString()}"
        holder.ratingText.text = currentItem.description.toString()
        holder.editBtn.setOnClickListener {
            val intent = Intent(holder.contexts, EditReview::class.java)
            intent.putExtra("reviewId", currentItem.reviewId)
            intent.putExtra("username", currentItem.username)
            intent.putExtra("docName", currentItem.docName)
            intent.putExtra("docUid", currentItem.docUid)
            intent.putExtra("userUid", currentItem.userUid)
            intent.putExtra("description", currentItem.description)
            intent.putExtra("stars", currentItem.noOfStars)
            holder.contexts.startActivity(intent)

        }

        holder.deleteBtn.setOnClickListener {
            val builder = AlertDialog.Builder(holder.contexts)
            builder.setTitle("Confirm Delete")
            builder.setMessage("Do you want to delete this item?")

            builder.setPositiveButton("Delete") { _, _ ->
                currentItem.reviewId?.let { reviewId ->
                    FirebaseDatabase.getInstance().getReference("Reviews")
                        .child(reviewId).removeValue().addOnSuccessListener {
                            Toast.makeText(holder.contexts, "Deleted Successfully", Toast.LENGTH_LONG)
                            val intent = Intent(holder.contexts, ShowReviews::class.java)
                            intent.putExtra("docUid", currentItem.docUid)
                            holder.contexts.startActivity(intent)

                        }.addOnFailureListener{
                            Toast.makeText(holder.contexts, "Delete Failed", Toast.LENGTH_LONG)
                        }
                }
            }

            builder.setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }

            builder.create().show()
        }
    }


    override fun getItemCount(): Int {
        return reviewList.size
    }

    class ReviewListViewHolder(itemView: View, listener: OnItemClickListener) : RecyclerView.ViewHolder(itemView) {
        val contexts: Context = itemView.context
        val user : TextView = itemView.findViewById(R.id.user)
        val doc: TextView = itemView.findViewById(R.id.reviewListItemDocName)
        val ratingText: TextView = itemView.findViewById(R.id.rating2_text2)
        val rate: RatingBar = itemView.findViewById(R.id.rating)
        val editBtn : ImageView = itemView.findViewById(R.id.edit3)
        val deleteBtn : ImageView = itemView.findViewById(R.id.delete3)

        init {
            itemView.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }
        }
    }

}
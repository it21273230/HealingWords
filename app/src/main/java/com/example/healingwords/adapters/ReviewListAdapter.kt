package com.example.healingwords.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.healingwords.EditReview
import com.example.healingwords.R
import com.example.healingwords.models.Review

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
        holder.user.text = "Anonymous"
        holder.ratingText.text = currentItem.description.toString()
        holder.editBtn.setOnClickListener {
            val intent = Intent(holder.contexts, EditReview::class.java)
            intent.putExtra("reviewId", currentItem.reviewId)
            intent.putExtra("docUid", currentItem.docUid)
            intent.putExtra("userUid", currentItem.userUid)
            intent.putExtra("description", currentItem.description)
            intent.putExtra("stars", currentItem.noOfStars)
            holder.contexts.startActivity(intent)

        }
    }


    override fun getItemCount(): Int {
        return reviewList.size
    }

    class ReviewListViewHolder(itemView: View, listener: OnItemClickListener) : RecyclerView.ViewHolder(itemView) {
        val contexts: Context = itemView.context
        val user : TextView = itemView.findViewById(R.id.user)
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
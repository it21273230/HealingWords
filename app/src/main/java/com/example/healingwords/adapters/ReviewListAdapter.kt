package com.example.healingwords.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.healingwords.R
import com.example.healingwords.models.Review

class ReviewListAdapter(private val reviewList: ArrayList<Review>) : RecyclerView.Adapter<ReviewListAdapter.ReviewListViewHolder>() {
    private lateinit var mListener: ReviewListAdapter.OnItemClickListener

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
        holder.rate.rating = currentItem.noOfStars!!.toFloat()
        holder.user.text = "Anonymous"
        holder.ratingText.text = currentItem.description

    }

    override fun getItemCount(): Int {
        return reviewList.size
    }

    class ReviewListViewHolder(itemView: View, listener: OnItemClickListener) : RecyclerView.ViewHolder(itemView) {
        val user : TextView = itemView.findViewById(R.id.user)
        val ratingText: TextView = itemView.findViewById(R.id.rating2_text2)
        val rate: RatingBar = itemView.findViewById(R.id.rating)

        init {
            itemView.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }
        }
    }

}
package com.example.healingwords

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text

class PostRecyclerAdapter(private val postList: List<Post>) :
    RecyclerView.Adapter<PostRecyclerAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val descView: TextView = itemView.findViewById(R.id.postDesc)

        fun setDescText(descText: String) {
            descView.text = descText
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.post_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var descData = postList.get(position).desc

        holder.setDescText(descData)
    }

    override fun getItemCount(): Int {
        // Return number of items here
        return postList.size
    }
}


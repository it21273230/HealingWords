package com.example.healingwords

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.healingwords.models.Blog

class BlogAdaptor(private val blogList : ArrayList<Blog>): RecyclerView.Adapter<BlogAdaptor.BlogViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlogViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.blog_item,parent,false)
        return BlogViewHolder(itemView)

    }

    override fun getItemCount(): Int {
        return blogList.size
    }

    override fun onBindViewHolder(holder: BlogViewHolder, position: Int) {

        val currentItem = blogList[position]

        holder.blogTitle.text = currentItem.title
        holder.desc.text = currentItem.desc
        holder.dcName.text = currentItem.docName

    }

    class BlogViewHolder (itemView: View): RecyclerView.ViewHolder(itemView){

        val blogTitle : TextView = itemView.findViewById(R.id.blogtitle)
        val desc : TextView = itemView.findViewById(R.id.blogdesc)
        val dcName : TextView = itemView.findViewById(R.id.docName)

    }

}
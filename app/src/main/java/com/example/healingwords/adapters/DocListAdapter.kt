package com.example.healingwords.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.healingwords.R
import com.example.healingwords.models.Doctor

class DocListAdapter(private val docList: ArrayList<Doctor>) : RecyclerView.Adapter<DocListAdapter.DocListViewHolder>() {

    class DocListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tvDocListDocName: TextView = itemView.findViewById(R.id.tvDocListDocName)
        val tvDocListRating:TextView = itemView.findViewById(R.id.tvDocListRating)
        val tvDocListProfession: TextView = itemView.findViewById(R.id.tvDocListProfession)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocListViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.doc_list_item, parent, false)
        return DocListViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return docList.size
    }

    override fun onBindViewHolder(holder: DocListViewHolder, position: Int) {
        val currentItem = docList[position]
        holder.tvDocListDocName.text = currentItem.name

        var rating = "${currentItem.rating}/10"
        holder.tvDocListRating.text = rating
        holder.tvDocListProfession.text = currentItem.title
    }
}
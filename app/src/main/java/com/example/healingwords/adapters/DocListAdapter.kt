package com.example.healingwords.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.healingwords.R
import com.example.healingwords.models.Doctor

class DocListAdapter(private val docList: ArrayList<Doctor>) : RecyclerView.Adapter<DocListAdapter.DocListViewHolder>(){
    private lateinit var mListener: OnItemClickListener
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        mListener = listener
    }

    class DocListViewHolder(itemView: View, listener: OnItemClickListener): RecyclerView.ViewHolder(itemView) {
        val tvDocListDocName: TextView = itemView.findViewById(R.id.tvDocListDocName)
        val tvDocListRating:TextView = itemView.findViewById(R.id.tvDocListRating)
        val tvDocListProfession: TextView = itemView.findViewById(R.id.tvDocListProfession)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocListViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.doc_list_item, parent, false)
        return DocListViewHolder(itemView, mListener)
    }

    override fun getItemCount(): Int {
        return docList.size
    }

    override fun onBindViewHolder(holder: DocListViewHolder, position: Int) {
        val currentItem = docList[position]
        holder.tvDocListDocName.text = currentItem.name
        val rating = if(currentItem.rating !=null){
            "${currentItem.rating}/10"
        } else {
            "0/10"
        }

        holder.tvDocListRating.text = rating
        holder.tvDocListProfession.text = currentItem.title
    }

}
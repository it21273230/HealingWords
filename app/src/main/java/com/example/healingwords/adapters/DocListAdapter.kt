package com.example.healingwords.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.healingwords.R
import com.example.healingwords.models.Doctor
import com.example.healingwords.models.Review
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.math.round

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
        setTotalRating(docUid = currentItem.uid!!, holder.tvDocListRating )
        holder.tvDocListProfession.text = currentItem.title
    }

    private fun setTotalRating(docUid: String,tvRating : TextView) {
        var totStars = 0.0
        var totGivenStars = 0.0
        var noOfReviews = 0.0
        var reviewDbRef =  FirebaseDatabase.getInstance().getReference("Reviews")
        reviewDbRef.addValueEventListener(object: ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(snapshot: DataSnapshot){
                if(snapshot.exists()) {
                    for(reviewSnapshot in snapshot.children) {
                        val review = reviewSnapshot.getValue(Review::class.java)
                        if(review!!.docUid == docUid) {
                            noOfReviews++
                            totStars += 5
                            totGivenStars += review.noOfStars!!.toInt()

                        }

                    }

                    val finalRating=((totGivenStars / (5*noOfReviews) )* 5)
                    tvRating.text = "$finalRating/5"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


    }
}
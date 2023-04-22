package com.example.healingwords

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healingwords.adapters.DocListAdapter
import com.example.healingwords.models.Doctor
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore

class DisplayDocList : Fragment() {

    private lateinit var docListRecyclerView: RecyclerView
    private var docList: ArrayList<Doctor> = arrayListOf()
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var docListAdapter: DocListAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_display_doc_list, container, false)

        docListRecyclerView = view.findViewById(R.id.rvDocListRecyclerView)
        docListRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
        docListAdapter = DocListAdapter(docList)

        docListRecyclerView.adapter = docListAdapter
        firebaseFirestore = FirebaseFirestore.getInstance()
        firebaseFirestore.collection("Doctors").addSnapshotListener { value, error ->
            if (error != null) {
                // Handle error
                return@addSnapshotListener
            }

            for (doc in value!!.documentChanges) {
                val doctor = doc.document.toObject(Doctor::class.java)



                when (doc.type) {
                    DocumentChange.Type.ADDED -> {
                       docList.add(doctor)

                      docListAdapter.notifyDataSetChanged()
                    }
                    else -> {
                        //handle something
                    }
                }
            }
            docListRecyclerView.adapter?.notifyDataSetChanged()
        }


        return view
    }





}
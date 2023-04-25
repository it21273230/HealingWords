package com.example.healingwords

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healingwords.adapters.DocListAdapter
import com.example.healingwords.models.Doctor
import com.google.firebase.database.*


class DisplayDocList : Fragment() {

    private lateinit var docListRecyclerView: RecyclerView
    private lateinit var docList: ArrayList<Doctor>
    private lateinit var dbRef: DatabaseReference



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_display_doc_list, container, false)

        docListRecyclerView = view.findViewById(R.id.rvDocListRecyclerView)
        docListRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
        docListRecyclerView.setHasFixedSize(true)

        docList = arrayListOf()
        docListRecyclerView.adapter = DocListAdapter(docList)
        getDoctorData()

        return view
    }

    private fun getDoctorData() {
        dbRef = FirebaseDatabase.getInstance().getReference("Doctors")

        dbRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()) {
                    for(doctorSnapshot in snapshot.children){
                        val doctor = doctorSnapshot.getValue(Doctor::class.java)
                        docList.add(doctor!!)
                    }
                    var adapter = DocListAdapter(docList)
                    docListRecyclerView.adapter = adapter
                    adapter.setOnItemClickListener(object : DocListAdapter.OnItemClickListener {
                        override fun onItemClick(position: Int) {
                            val intent = Intent(requireActivity(), UserViewDocProfile::class.java)
                            intent.putExtra("uid", docList[position].uid)
                            startActivity(intent)

                        }

                    })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }


}
package com.example.healingwords

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healingwords.models.Blog
import com.google.firebase.database.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class BlogFragment : Fragment() {
    private lateinit var dbRef : DatabaseReference
    private lateinit var blogRecyclerView: RecyclerView
    private lateinit var blogArrayList: ArrayList<Blog>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val view = inflater.inflate(R.layout.fragment_blog, container, false)

        blogRecyclerView = view.findViewById(R.id.blogList)
        blogRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        blogRecyclerView.setHasFixedSize(true)

        blogArrayList = arrayListOf<Blog>()
        getBlogData()

        return view
    }

    private fun getBlogData() {

        dbRef = FirebaseDatabase.getInstance().getReference("Blogs")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if(snapshot.exists()){
                    for (userSnapshot in snapshot.children){

                        val blog = userSnapshot.getValue(Blog::class.java)
                        blogArrayList.add(blog!!)

                    }

                    blogRecyclerView.adapter = BlogAdaptor(blogArrayList)
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

    }


}
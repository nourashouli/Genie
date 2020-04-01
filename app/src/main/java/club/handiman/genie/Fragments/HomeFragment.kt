package com.example.genie_cl.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.genie_cl.MainActivity
import android.content.Intent
import club.handiman.genie.TestingActivity
import com.example.genie_cl.R
import com.example.genie_cl.adapter.HomeAdapter
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    } // onCreateView

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initRecyclerView()
        requestbt.setOnClickListener {
            val intent = Intent(context!!, TestingActivity::class.java)
            intent.flags =
                Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)


        }
    } // onActivityCreated

    private fun initRecyclerView(){

        home_recycler_view.layoutManager = LinearLayoutManager(context as MainActivity,LinearLayoutManager.VERTICAL,false)
        home_recycler_view.adapter = HomeAdapter(context as MainActivity,"home")

    }


} // HomeFragment

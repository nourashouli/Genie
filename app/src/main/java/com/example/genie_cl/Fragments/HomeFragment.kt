package com.example.genie_cl.Fragments

import android.content.res.Resources
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.genie_cl.MainActivity

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

    } // onActivityCreated

    private fun initRecyclerView(){

        home_recycler_view.layoutManager = LinearLayoutManager(context as MainActivity,LinearLayoutManager.VERTICAL,false)
        home_recycler_view.adapter = HomeAdapter(context as MainActivity,"home")

    }


} // HomeFragment

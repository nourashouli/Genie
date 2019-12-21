package com.example.genie_cl.Fragments

import android.content.Context
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.genie_cl.MainActivity

import com.example.genie_cl.R
import com.example.genie_cl.adapter.HomeAdapter
import kotlinx.android.synthetic.main.fragment_handymanprofile.*


class HandymanprofileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_handymanprofile, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initRecyclerView()

    }

    private fun initRecyclerView(){
        images_recycle_view_id.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        images_recycle_view_id.adapter = HomeAdapter(context as MainActivity, "handyman")
    }

}

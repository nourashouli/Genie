package com.example.genie_cl.Fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.genie_cl.MainActivity

import com.example.genie_cl.R
import com.example.genie_cl.adapter.SearchAdapter


/**
 * A simple [Fragment] subclass.
 */
class SearchFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    } // onCreateView

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


    } // onActivityCreated




} // HomeFragment

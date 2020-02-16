package com.example.genie_cl.Fragments


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.genie_cl.Login
import com.example.genie_cl.MainActivity

import com.example.genie_cl.R
import com.example.genie_cl.adapter.SearchAdapter
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_search.*


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

        electrical.setOnClickListener {
            (context as MainActivity).navigateToFragment(handymanlist())
//assume you wrotte a line of code here  and in the same time I am writing in the same page
            // you want to push to the github your code but I already chnaged in t o in this case check what I am gonna do ok

        }
    } // onActivityCreated

// Mhmd testt meshe hala


} // HomeFragment

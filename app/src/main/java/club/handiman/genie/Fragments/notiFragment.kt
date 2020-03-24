package com.example.genie_cl.Fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.genie_cl.MainActivity

import com.example.genie_cl.R
import com.example.genie_cl.adapter.HomeAdapter
import com.example.handymanapplication.adapters.NotificationAdapter
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_noti.*

/**
 * A simple [Fragment] subclass.
 */
class notiFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_noti, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initRecycler()

    }

    private fun initRecycler(){
        recycler_view_notifications.layoutManager = LinearLayoutManager(context as MainActivity,
            LinearLayoutManager.VERTICAL,false)
        recycler_view_notifications.adapter = NotificationAdapter(context as MainActivity)
    }


}

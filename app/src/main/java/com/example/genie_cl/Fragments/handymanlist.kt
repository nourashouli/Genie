package com.example.genie_cl.Fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.genie_cl.R
import kotlinx.android.synthetic.main.fragment_handymanlist.*

/**
 * A simple [Fragment] subclass.
 */
class handymanlist : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       // val text:String? = arguments!!.getString("category")
//category.setText(text)
// Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_handymanlist, container, false)
    }
}

/*
}
 public View onCreateView(LayoutInflater inflater, ViewGroup container,
    Bundle savedInstanceState) {
          String strtext=getArguments().getString("message");

    return inflater.inflate(R.layout.fragment, container, false);
 */

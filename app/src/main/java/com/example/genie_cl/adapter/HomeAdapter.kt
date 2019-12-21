package com.example.genie_cl.adapter

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.genie_cl.Fragments.HandymanprofileFragment
import com.example.genie_cl.MainActivity
import com.example.genie_cl.R
import kotlinx.android.synthetic.main.home_row.view.*

class HomeAdapter(val context: Context, val fragmentName: String) : RecyclerView.Adapter<HomeAdapter.HomeAdapterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeAdapterViewHolder {
        return HomeAdapterViewHolder(LayoutInflater.from(context).inflate(R.layout.home_row, parent, false))
    }

    override fun getItemCount(): Int {
        return 5
    }

    override fun onBindViewHolder(holder: HomeAdapterViewHolder, position: Int) {


        if (fragmentName == "home"){

            holder.mainCardView.setOnClickListener {
                (context as MainActivity).navigateToFragment(HandymanprofileFragment())
            }

        } else if("handyman" == fragmentName){

            holder.image.visibility = View.GONE
            holder.txt.visibility = View.GONE

        }

    }

    inner class HomeAdapterViewHolder(view: View): RecyclerView.ViewHolder(view){

        val mainCardView = view.main_card_view
        val image = view.imageView2
        val txt = view.textView


    }

}
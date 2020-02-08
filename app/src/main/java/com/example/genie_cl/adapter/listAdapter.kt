package com.devosha.kotlin_recyclerview_sort

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import java.util.ArrayList
import com.example.genie_cl.R
import android.view.View.inflate
import android.widget.TextView

/**
 * class MyAdapter.
 * This class is our MyAdapter class.
 * It will ReyclerView.Adapter class. Via the constructor we will pass a Context,
 * and an ArrayList of spacecrafts.
 */
class listAdapter(internal var c: Context, internal var spacecrafts: ArrayList<String>) : RecyclerView.Adapter<listAdapter.MyViewHolder>() {
    /*
    class: MyViewHolder
    This is our View Holder class.
   */
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var nameTxt: TextView = itemView.findViewById(R.id.nameTxt)
    }

    override fun getItemCount(): Int {
        return spacecrafts.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(c).inflate(R.layout.handyman_row, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //BIND DATA
        holder.nameTxt.text = spacecrafts[position]
    }
}
//end
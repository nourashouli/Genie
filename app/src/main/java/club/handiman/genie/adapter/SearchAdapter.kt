package com.example.genie_cl.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.genie_cl.R

class SearchAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchAdapterViewHolder {
        return SearchAdapterViewHolder(LayoutInflater.from(context).inflate(R.layout.handyman_row, parent, false))
    }

    override fun getItemCount(): Int {
        return 5
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

    }

    class SearchAdapterViewHolder(view: View): RecyclerView.ViewHolder(view){



    }

}
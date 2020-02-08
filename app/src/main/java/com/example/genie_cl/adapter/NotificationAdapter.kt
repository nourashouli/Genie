package com.example.genie_cl.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.genie_cl.R
import kotlinx.android.synthetic.main.home_row.view.*

class NotificationAdapter(val context: Context) : RecyclerView.Adapter<NotificationAdapter.NotificationAdapterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationAdapterViewHolder {
        return NotificationAdapterViewHolder(LayoutInflater.from(context)
            .inflate(R.layout.notification_row, parent, false))
    }


    override fun getItemCount(): Int {
        return 5
    }

    override fun onBindViewHolder(holder: NotificationAdapterViewHolder, position: Int) {




    }

    inner class NotificationAdapterViewHolder(view: View): RecyclerView.ViewHolder(view){



    }
}
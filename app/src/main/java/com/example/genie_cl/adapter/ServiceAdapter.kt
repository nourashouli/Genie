package com.example.genie_cl.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.genie_cl.R

import android.view.View
import com.bumptech.glide.Glide
import com.example.genie_cl.Models.CardView
import com.example.genie_cl.Utils.Utils
import kotlinx.android.synthetic.main.service_card.view.*

class ServiceAdapter(context: Context) : RecyclerView.Adapter<ServiceAdapter.ViewHolder>() {
    var list: ArrayList<Any> = ArrayList()

    fun setItem(ob: Any) {
        list.add(ob)
        notifyItemInserted(list.size - 1)
    }

    fun getItem(index: Int) = list[index]

    fun removeItem(index: Int) {
        list.removeAt(index)
        notifyItemRemoved(index)
    }

    fun removeItems() {
        list.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.service_card, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.itemView.service_title.text = "123"

//        holder.itemView.service_title.text = (list[position] as CardView).service_title
//        holder.itemView.service_count.setText((list[position] as CardView).service_count)
//        val image_url = (list[position] as CardView).service_picture
//        val url =
//            Utils.BASE_IMAGE_URL.plus(image_url)

//        Glide
//            .with(holder.itemView)
//            .load(image_url).into(holder.itemView.service_image)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

}

/*

 */
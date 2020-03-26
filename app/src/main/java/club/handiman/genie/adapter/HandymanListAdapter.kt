package com.example.genie_cl.adapter

import android.content.ContentResolver
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SortedList
import com.bumptech.glide.Glide
import com.example.genie_cl.Fragments.HomeFragment
import com.example.genie_cl.Fragments.handymanlist
import com.example.genie_cl.Utils.Utils
import kotlinx.android.synthetic.main.fragment_handymanlist.view.*
import com.example.genie_cl.R
import com.example.genie_cl.requestForm2
import kotlinx.android.synthetic.main.handyman_row.view.*
import org.json.JSONObject


class HandymanListAdapter(var context: Context) :
    RecyclerView.Adapter<HandymanListAdapter.ViewHolder>() {
    var list: ArrayList<Any> = ArrayList()
    var sortedlist: ArrayList<Any> = ArrayList()
    // var context:Context = context
    fun setItem(ob: Any) {
        list.add(ob)
        sortedlist.add(ob)
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
                .inflate(R.layout.handyman_row, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if ((list[position] as JSONObject).getString("name") != "") {
            holder.itemView.handyman_name.text = (list[position] as JSONObject).getString("name")

        }

        if (((list[position] as JSONObject).has("image"))) {


            val image_url = (list[position] as JSONObject).getString("image")

            Glide
                .with(holder.itemView)
                .load(Utils.BASE_IMAGE_URL.plus(image_url))
                .into(holder.itemView.handyman_profile_picture)

        }
    }
//generic sort
    fun sort(value : String, order : String = "asc" ) {
       if ( order == "asc"){
           list.sortBy {
               (it as JSONObject).getString(value)
           }
       }else{
           list.sortByDescending {
               (it as JSONObject).getString(value)
           }
       }
    // [long , latit]
    notifyDataSetChanged()
    }
    // }


    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //itemView.setOnClickListener( {itemClick(layoutPosition)} )
    }
}
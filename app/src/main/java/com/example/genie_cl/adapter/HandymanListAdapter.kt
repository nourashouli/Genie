package com.example.genie_cl.adapter

import android.content.ContentResolver
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.genie_cl.Fragments.HomeFragment
import com.example.genie_cl.Fragments.handymanlist
import com.example.genie_cl.MainActivity
import com.example.genie_cl.R
import com.example.genie_cl.Utils.Utils
import kotlinx.android.synthetic.main.fragment_handymanlist.*
import kotlinx.android.synthetic.main.handyman_row.view.*
import org.json.JSONObject


 class HandymanListAdapter(var context : Context) : RecyclerView.Adapter<HandymanListAdapter.ViewHolder>() {
    var list: ArrayList<Any> = ArrayList()
    // var context:Context = context
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
                .inflate(R.layout.handyman_row, parent, false)
        )
    }

     override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.name.text = (list[position] as JSONObject).getString("name")
        val image_url = (list[position] as JSONObject).getString("profile_picture")

        Glide
            .with(holder.itemView)
            .load(Utils.BASE_IMAGE_URL.plus(image_url)).into(holder.itemView.imageView2)
        holder.itemView.imageView2.setOnClickListener{

            //{itemClick(layoutPosition)}
            (context as MainActivity).navigateToFragment(HomeFragment())




//            val bundle:Bundle = context.instance
//            ba
//            bundle.putString("category", (list[position] as JSONObject).getString("name"))
//            //set Fragmentclass Arguments
//            //set Fragmentclass Arguments
//            val fragobj = handymanlist()
//            fragobj.setArguments(bundle)





        }
    }


    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){

        // itemView.setOnClickListener( {itemClick(layoutPosition)} )
    }

}
package com.example.genie_cl.adapter
import com.example.genie_cl.Fragments.handymanlist
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.genie_cl.MainActivity
import com.example.genie_cl.R
import com.example.genie_cl.Utils.Utils
import com.example.genie_cl.adapter.utils.AdapterListener
import kotlinx.android.synthetic.main.service_card.view.*
import org.json.JSONObject


class ServiceAdapter(var context : Context) : RecyclerView.Adapter<ServiceAdapter.ViewHolder>() {
    var list: ArrayList<Any> = ArrayList()
    var listener: AdapterListener? = null

    constructor(  context: Context , listener: AdapterListener) : this(context) {
        this.listener = listener
    }


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
                .inflate(R.layout.service_card, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.service_title.text = (list[position] as JSONObject).getString("name")
        val image_url = (list[position] as JSONObject).optString("image","image.png")

        Glide
            .with(holder.itemView)
            .load(Utils.BASE_IMAGE_URL.plus(image_url)).into(holder.itemView.service_image)
        holder.itemView.service_image.setOnClickListener{

//            //{itemClick(layoutPosition)}
            (context as MainActivity).navigateToFragment(handymanlist((list[position])))
//            this.listener!!.onAction(list[position])
        }
    }


    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){

       // itemView.setOnClickListener( {itemClick(layoutPosition)} )
    }

}

/*

 */
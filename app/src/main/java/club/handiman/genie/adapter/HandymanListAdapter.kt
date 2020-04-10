package club.handiman.genie.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import club.handiman.genie.Fragments.HandymanprofileFragment
import club.handiman.genie.MainActivity

import club.handiman.genie.Utils.Utils
import com.example.genie_cl.R
import kotlinx.android.synthetic.main.handyman_row.view.*
import org.json.JSONObject



class HandymanListAdapter(var context : Context,var id:String) : RecyclerView.Adapter<HandymanListAdapter.ViewHolder>() {
    var list: ArrayList<Any> = ArrayList()
    var service_id: String? = id
    var sortedlist: ArrayList<Any> = ArrayList()

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
        if ((list[position] as JSONObject).getString("name") != "") {

            holder.itemView.handyman_name.text = (list[position] as JSONObject).getString("name")

        }

                val image_url = (list[position] as JSONObject).optString("image")
                Glide
                    .with(holder.itemView)
                    .load(Utils.BASE_IMAGE_URL.plus(image_url))
                    .into(holder.itemView.handyman_profile_picture)
            holder.itemView.setOnClickListener {

                // {itemClick(layoutPosition)}
                (context as MainActivity).navigateToFragment(
                    HandymanprofileFragment(
                        (list[position]),
                        service_id!!
                    )
                )
//           this.listener!!.onAction(list[position])
            }




    //generic sort
    fun sort(value: String, order: String = "asc") {
        if (order == "asc") {
            list.sortBy {
                (it as JSONObject).getString(value)
            }
        } else {
            list.sortByDescending {
                (it as JSONObject).getString(value)
            }
        }
        // [long , latit]
        notifyDataSetChanged()




    }





        }




    fun sort(value: String, order: String = "asc") {
        if (order == "asc") {
            list.sortBy {
                (it as JSONObject).getString(value)
            }
        } else {
            list.sortByDescending {
                (it as JSONObject).getString(value)
            }
        }
        // [long , latit]
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //itemView.setOnClickListener( {itemClick(layoutPosition)} )
    }
}

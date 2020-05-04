package club.handiman.genie.adapter


import android.content.Context
import android.location.Location
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
import org.jetbrains.anko.runOnUiThread
import org.json.JSONObject


class HandymanListAdapter(var context: Context, var id: String) :
    RecyclerView.Adapter<HandymanListAdapter.ViewHolder>() {
    var list: ArrayList<Any> = ArrayList()
    var service_id: String? = id
    var sortedlist: ArrayList<Any> = ArrayList()
    var query: String? = null
    var filterlist: ArrayList<Any> = ArrayList()
    var date: String? = null
    var datelist: ArrayList<Any> = ArrayList()
    var day: Int = 0


    val d = FloatArray(1)
    // var context:Context = context
    fun setItem(ob: Any) {
        list.add(ob)
//
        filterlist.add(ob)
        sortedlist.add(ob)
        datelist.add(ob)
        notifyItemInserted(list.size - 1)
        notifyItemInserted(filterlist.size - 1)
        notifyItemInserted(datelist.size - 1)

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
        context!!.runOnUiThread {
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //itemView.setOnClickListener( {itemClick(layoutPosition)} )
    }

    fun locationsort(userlat: Double, userlon: Double, lat: String, lon: String, order: String) {
        if (order == "asc") {
            list.sortBy {
                distanceikm(
                    userlat,
                    userlon,
                    (it as JSONObject).getJSONArray(lat).get(0) as Double,
                    (it as JSONObject).getJSONArray(lon)
                        .get(1) as Double
                )


            }
        } else {
            list.sortByDescending {
                distanceikm(
                    userlat,
                    userlon,
                    (it as JSONObject).getJSONArray(lat).get(0) as Double,
                    (it as JSONObject).getJSONArray(lon)
                        .get(1) as Double
                )
            }
        }
        notifyDataSetChanged()


    }

    fun distanceikm(lat1: Double, lon2: Double, lat3: Double, lon4: Double): Float {

        Location.distanceBetween(lat1, lon2, lat3, lon4, d)
        return d[0]

    }

    fun filter(key: String, query: String) {
        this.query = query
        this.list.clear()
        this.list.addAll(this.filterlist.filter {
            (it as JSONObject).optString(key).contains(query)
        })

        notifyDataSetChanged()
    }
}
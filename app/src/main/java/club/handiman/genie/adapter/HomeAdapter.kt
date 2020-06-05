package club.handiman.genie.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import club.handiman.genie.Utils.Utils
import com.bumptech.glide.Glide
import com.example.genie_cl.R
import com.example.genie_cl.adapter.utils.AdapterListener
import kotlinx.android.synthetic.main.home_row.view.*
import org.json.JSONObject


class HomeAdapter(var context: Context) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {
    var list: ArrayList<Any> = ArrayList()
    var listener: AdapterListener? = null
    var service ="h"

    constructor(context: Context, listener: AdapterListener) : this(context) {
        this.listener = listener
    }
//id , employee-profille (Json
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
                .inflate(R.layout.home_row, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var handyman =
            (list[position] as JSONObject).getJSONObject("handyman").optString("name", "handyman")
//        service =
//            (list[position] as JSONObject).getJSONObject("services").optString("name", "services")
        holder.itemView.HandymaName.text = handyman
        holder.itemView.caption.text = (list[position] as JSONObject).optString("body")
        holder.itemView.timecreated.text = (list[position] as JSONObject).optString("created_at")

        if ((list[position] as JSONObject).has("images")) {
            //for post images
            var images_array = (list[position] as JSONObject).getJSONArray("images")
            val url = images_array.get(0).toString()

            Glide
                .with(holder.itemView)
                .load(Utils.BASE_IMAGE_URL.plus(url))
                .into(holder.itemView.HandymaUpload)
        }


        val image_url =
            (list[position] as JSONObject).getJSONObject("handyman").optString("image", "image.png")


//
        Glide
            .with(holder.itemView)
            .load(Utils.BASE_IMAGE_URL.plus(image_url)).into(holder.itemView.handymanImage)

        val clickListener = View.OnClickListener { view ->
            when (view.id) {
                R.id.tagg -> {
                    showPopup(view)


                }
            }

        }
        holder.itemView.tagg.setOnClickListener(clickListener)

        }



    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        // itemView.setOnClickListener( {itemClick(layoutPosition)} )
    }
    fun showPopup(view: View) {
        var popup: PopupMenu? = null;
        popup = PopupMenu(context, view)
        popup.inflate(R.menu.category_menu)

        popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem? ->
              when (item!!.itemId) {
                  R.id.category1 -> {

                          item.setTitle(service);
                  }
//                   R.id.header2 -> {
//                       Toast.makeText(this@MainActivity, item.title, Toast.LENGTH_SHORT).show();
//                   }
//                   R.id.header3 -> {
//                       Toast.makeText(this@MainActivity, item.title, Toast.LENGTH_SHORT).show();
//                   }
//               }
              }
            true
        })

        popup.show()
    }
}






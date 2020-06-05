package club.handiman.genie.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import club.handiman.genie.Fragments.HandymanprofileFragment
import club.handiman.genie.MainActivity
import club.handiman.genie.Utils.Utils
import com.bumptech.glide.Glide
import com.example.genie_cl.R
import com.example.genie_cl.adapter.utils.AdapterListener
import kotlinx.android.synthetic.main.home_row.view.*
import org.json.JSONArray
import org.json.JSONObject


class HomeAdapter(var context: Context) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {
    var list: ArrayList<Any> = ArrayList()
    var listener: AdapterListener? = null
   var items: JSONArray? =null
    constructor(context: Context, listener: AdapterListener) : this(context) {
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
                .inflate(R.layout.home_row, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var view: View=View(context)
        var popup: PopupMenu? = null;
        popup = PopupMenu(context, view)
        popup.inflate(R.menu.category_menu)
        var handyman =
            (list[position] as JSONObject).getJSONObject("handyman").optString("name", "handyman")
         items =  (list[position] as JSONObject).getJSONArray("services")

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
                    var popup: PopupMenu? = null;
                    popup = PopupMenu(context, view)
                    popup!!.inflate(R.menu.category_menu)
                    for (i in 0 until items!!.length()) {
                        popup!!.menu.add(items!!.getJSONObject(i).optString("name"))

                    }
                    popup!!.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem? ->
//
                        if (item!!.title == "g") {

                        }
                        else{   (context as MainActivity).navigateToFragment(
                            HandymanprofileFragment(
                                (list[position] as JSONObject).getJSONObject("handyman"),
                                (list[position] as JSONObject).getJSONArray("services").getJSONObject(1).optString("_id")
                            )
                        )}

                        true
                    })

                    popup!!.show()



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
    }}
//    fun showPopup(view: View) {
//        var popup: PopupMenu? = null;
//        popup = PopupMenu(context, view)
//        popup.inflate(R.menu.category_menu)
//        for (i in 0 until items!!.length()) {
//            popup.menu.add(items!!.getJSONObject(i).optString("name"))
//
//        }
//            popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem? ->
//
//                    if (item!!.title == "Carpenter") {
//                        (context as MainActivity).navigateToFragment(
//                            HandymanprofileFragment(
//                                (list[position] as JSONObject).getJSONObject("handyman"),
//                                "5ecd92a7e8deab7c2f7962b2"
//                            )
//                        )
//                    }
//else{  Toast.makeText(context, "tiling", Toast.LENGTH_LONG)
//                        .show()}
//
//            true
//        })
//
//        popup.show()
//    }
//}






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

    var _list: ArrayList<Any> = ArrayList()
    var list: ArrayList<Any> = ArrayList()
    var query: String? = null
    var listener: AdapterListener? = null
    var items: JSONArray? = null

    constructor(context: Context, listener: AdapterListener) : this(context) {
        this.listener = listener
    }

    //id , employee-profille (Json
    // var context:Context = context
    fun setItem(ob: Any) {
        _list.add(ob)
        notifyItemInserted(_list.size - 1)
    }

    fun getItem(index: Int) = _list[index]

    fun removeItem(index: Int) {
        _list.removeAt(index)
        notifyItemRemoved(index)
    }

    fun removeItems() {
        _list.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.home_row, parent, false)
        )
    }

    fun filter(key: String, query: String) {
        this.query = query
        this.list.clear()

        for (items in 0 until _list.size) {
            val services = (_list[items] as JSONObject).optJSONArray("services")
            for (j in 0 until services.length()) {
                if ((services!!.optJSONObject(j).optString("name")).toLowerCase().contains(query.toLowerCase())) {
                    list.add(_list[items])
                }
            }
        }

        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (query == null) {
            var view: View = View(context)
            var popup: PopupMenu? = null;
            popup = PopupMenu(context, view)
            popup.inflate(R.menu.category_menu)
            var handyman =
                (_list[position] as JSONObject).getJSONObject("handyman")
                    .optString("name", "handyman")
            items = (_list[position] as JSONObject).getJSONArray("services")

            holder.itemView.HandymaName.text = handyman
            holder.itemView.caption.text = (_list[position] as JSONObject).optString("body")
            holder.itemView.timecreated.text =
                (_list[position] as JSONObject).optString("created_at")

            if ((_list[position] as JSONObject).has("images")) {
                //for post images
                var images_array = (_list[position] as JSONObject).getJSONArray("images")
                val url = images_array.get(0).toString()

                Glide
                    .with(holder.itemView)
                    .load(Utils.BASE_IMAGE_URL.plus(url))
                    .into(holder.itemView.HandymaUpload)
            }


            val image_url =
                (_list[position] as JSONObject).getJSONObject("handyman")
                    .optString("image", "image.png")


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

                            } else {
                                var employeeid =
                                    (_list[position] as JSONObject).getJSONObject("handyman")
                                        .optString("_id")
                                var service_id =
                                    (_list[position] as JSONObject).getJSONArray("services")
                                        .getJSONObject(1).optString("_id")
                                (context as MainActivity).navigateToFragment(
                                    HandymanprofileFragment(
                                        Helpers.EmployeeProfile.employeeprofile(
                                            employeeid,
                                            context!!
                                        ) as Any, service_id
                                    )
                                )
                            }

                            true
                        })

                        popup!!.show()


                    }
                }

            }
             holder.itemView.tagg.setOnClickListener(clickListener)
        }else{
            var view: View = View(context)
            var popup: PopupMenu? = null;
            popup = PopupMenu(context, view)
            popup!!.inflate(R.menu.category_menu)
            var handyman =
                (list[position] as JSONObject).getJSONObject("handyman")
                    .optString("name", "handyman")
            items = (list[position] as JSONObject).getJSONArray("services")

            holder.itemView.HandymaName.text = handyman
            holder.itemView.caption.text = (list[position] as JSONObject).optString("body")
            holder.itemView.timecreated.text =
                (list[position] as JSONObject).optString("created_at")

            if ((list[position] as JSONObject).has("images")) {
                //for post images
                var images_array = (_list[position] as JSONObject).getJSONArray("images")
                val url = images_array.get(0).toString()

                Glide
                    .with(holder.itemView)
                    .load(Utils.BASE_IMAGE_URL.plus(url))
                    .into(holder.itemView.HandymaUpload)
            }


            val image_url =
                (list[position] as JSONObject).getJSONObject("handyman")
                    .optString("image", "image.png")


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

                            } else {
                                var employeeid =
                                    (list[position] as JSONObject).getJSONObject("handyman")
                                        .optString("_id")
                                var service_id =
                                    (list[position] as JSONObject).getJSONArray("services")
                                        .getJSONObject(1).optString("_id")
                                (context as MainActivity).navigateToFragment(
                                    HandymanprofileFragment(
                                        Helpers.EmployeeProfile.employeeprofile(
                                            employeeid,
                                            context!!
                                        ) as Any, service_id
                                    )
                                )
                            }

                            true
                        })

                        popup!!.show()


                    }
                }

            }
            holder.itemView.tagg.setOnClickListener(clickListener)
        }
    }


    override fun getItemCount(): Int {
        return _list.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        // itemView.setOnClickListener( {itemClick(layoutPosition)} )
    }
}
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






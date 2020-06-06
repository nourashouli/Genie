package club.handiman.genie.adapter

import club.handiman.genie.Fragments.handymanlist
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import club.handiman.genie.MainActivity
import com.example.genie_cl.R
import club.handiman.genie.Utils.Utils
import club.handiman.genie.Utils.putExtraJson
import club.handiman.genie.requestForm
import com.example.genie_cl.adapter.utils.AdapterListener
import kotlinx.android.synthetic.main.service_card.view.*
import org.json.JSONObject


class ServiceAdapter(var context: Context) : RecyclerView.Adapter<ServiceAdapter.ViewHolder>() {
    var list: ArrayList<Any> = ArrayList()
    var listener: AdapterListener? = null

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
                .inflate(R.layout.service_card, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.itemView.overflow.setOnClickListener {
//            val popupMenu: PopupMenu = PopupMenu(context!!, holder.itemView.overflow)
//            popupMenu.menuInflater.inflate(R.menu.menu_album, popupMenu.menu)
//            popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
//                when (item.itemId) {
//                    R.id.urgent_request -> {
//                        val intent = Intent(context!!, requestForm::class.java)
//                        var ob = JSONObject()
//                        ob.put("id", (list[position] as JSONObject).optString("_id"))
//                        intent.putExtraJson("object", ob!!)
//                        ContextCompat.startActivity(context!!, intent!!, null)
//                        Toast.makeText(context!!, "You Clicked : " + item.title, Toast.LENGTH_SHORT)
//                            .show()
//                    }
//                }
//                true
//            })
//            popupMenu.show()
//
//        }
        holder.itemView.service_title.text = (list[position] as JSONObject).getString("name")
        val image_url = (list[position] as JSONObject).optString("image", "image.png")

        Glide
            .with(holder.itemView)
            .load(Utils.BASE_IMAGE_URL.plus(image_url)).into(holder.itemView.service_image)
        holder.itemView.service_image.setOnClickListener {

            //            //{itemClick(layoutPosition)}
            (context as MainActivity).navigateToFragment(handymanlist((list[position])))
//            this.listener!!.onAction(list[position])
        }
    }


    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        // itemView.setOnClickListener( {itemClick(layoutPosition)} )
    }

}

/*

 */
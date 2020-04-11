package club.handiman.genie.adapter

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import club.handiman.genie.Utils.Utils
import com.bumptech.glide.Glide
import com.example.genie_cl.R
import com.example.genie_cl.adapter.utils.AdapterListener
import kotlinx.android.synthetic.main.home_row.view.*
import org.json.JSONObject

class HomeAdapter(var context : Context) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {
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
                .inflate(R.layout.home_row, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.itemView.HandymaName.text = (list[position] as JSONObject).getString("name")


        val image_url = (list[position] as JSONObject).optString("image","image.png")

        Glide
            .with(holder.itemView)
            .load(Utils.BASE_IMAGE_URL.plus(image_url)).into(holder.itemView.handymanImage)
        holder.itemView.handymanImage.setOnClickListener{
        }
    }


    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){

        // itemView.setOnClickListener( {itemClick(layoutPosition)} )
    }

}
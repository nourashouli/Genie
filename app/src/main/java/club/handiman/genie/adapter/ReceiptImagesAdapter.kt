package club.handiman.genie.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import club.handiman.genie.Models.RequestModel
import com.bumptech.glide.Glide
import com.example.genie_cl.R
import club.handiman.genie.Utils.Utils
import kotlinx.android.synthetic.main.fragment_images_adapter.view.*
import kotlinx.android.synthetic.main.post_image.view.*
import kotlinx.android.synthetic.main.service_card.view.*
import org.json.JSONArray

public  class ReceiptImagesAdapter(var context: Context) : RecyclerView.Adapter<ReceiptImagesAdapter.ViewHolder>() {

    var list: ArrayList<Any> = ArrayList()

    fun setItem(ob:Any) {
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

    fun getItems() = list


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.post_image, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val url = (list[position]  ).toString()
        Glide
            .with(holder.itemView)
            .load(Utils.BASE_IMAGE_URL.plus(url))
            .into(holder.itemView.create_post_image)



    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

}


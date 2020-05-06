package club.handiman.genie.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import club.handiman.genie.Models.RequestModel
import com.bumptech.glide.Glide
import com.example.genie_cl.R
import club.handiman.genie.Utils.Utils
import kotlinx.android.synthetic.main.notification_row.view.*

class notiAdapter(var context: Context) : RecyclerView.Adapter<notiAdapter.ViewHolder>() {

    var list: ArrayList<RequestModel> = ArrayList()

    fun setItem(ob: RequestModel) {
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
                .inflate(R.layout.notification_row, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.name.text =
            (list[position]).handyman
        holder.itemView.date.text =
            (list[position]).date
        holder.itemView.from.text =
            (list[position]).from
        holder.itemView.to.text =
            (list[position]).to
        holder.itemView.textView.text =
            (list[position]).request
        val url = (list[position]).image
        Glide
            .with(holder.itemView)
            .load(Utils.BASE_IMAGE_URL.plus(url))
            .into(holder.itemView.imageView2)

        holder.itemView.cancel.setOnClickListener {

               Helpers.RequestHelper.cancel((list[position]).request_id,context!!)
            list.remove(list[position])
            notifyDataSetChanged()
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

}


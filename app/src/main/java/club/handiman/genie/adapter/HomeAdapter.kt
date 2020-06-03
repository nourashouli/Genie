package club.handiman.genie.adapter

import android.content.Context
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
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.stream.Collectors

class HomeAdapter(var context: Context) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {
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
                .inflate(R.layout.home_row, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var handyman =
            (list[position] as JSONObject).getJSONObject("handyman").optString("name", "handyman")

        holder.itemView.HandymaName.text = handyman

        holder.itemView.timecreated.text= (list[position] as JSONObject).optString("created_at")
//        val formatter =
//            DateTimeFormatter.ofPattern("yyyy-MM-ss HH:mm:ss")
//        val dateTimes: List<LocalDateTime> =
//            list.stream().map({ date -> LocalDateTime.parse(date as CharSequence?, formatter) }).sorted()
//                .collect(Collectors.toList())
//        println(dateTimes)
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


    }


    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        // itemView.setOnClickListener( {itemClick(layoutPosition)} )
    }

}






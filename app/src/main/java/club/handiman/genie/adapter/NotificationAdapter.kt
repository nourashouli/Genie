package club.handiman.genie.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.genie_cl.R
import club.handiman.genie.Utils.Utils
import kotlinx.android.synthetic.main.notification_row.view.*
import org.json.JSONObject

class NotificationAdapter(var context: Context) : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    var list : ArrayList<Any> = ArrayList()

    fun setItem( ob: Any){
        list.add(ob)
        notifyItemInserted(list.size -1)
    }
    fun getItem( index : Int) = list[index]

    fun removeItem (index: Int){
        list.removeAt(index)
        notifyItemRemoved( index )
    }
    fun removeItems (){
        list.clear()
        notifyDataSetChanged()
    }

    fun getItems() = list



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.notification_row,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.itemView.setOnClickListener {
//          //  if ((list[position] as JSONObject).)
//        }

//        holder.itemView.textView.text=
//            (list[position] as JSONObject).optString("description", "title")
       holder.itemView.name.text=
           (list[position] as JSONObject).optString("email", "name")
        val url = (list[position] as JSONObject).optString("image", "image.png")

        Toast.makeText(context,
            (list[position]).toString()  ,
            Toast.LENGTH_LONG
        ).show()
        Glide
            .with(holder.itemView)
            .load(Utils.BASE_IMAGE_URL.plus(url))
            .into(holder.itemView.imageView2)
//
//      //
//        if ( query == null )
//        holder.itemView.text1.text = (_list[position] as JSONObject).optString("name","unknown")
//        else
//        holder.itemView.text1.text = (list[position] as JSONObject).optString("name","unknown")
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view)

}


package club.handiman.genie.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.genie_cl.R
import kotlinx.android.synthetic.main.clientaddress.view.*
import org.json.JSONObject

class locationAdapter(var context: Context) : RecyclerView.Adapter<locationAdapter.ViewHolder>() {

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
                .inflate(R.layout.clientaddress, parent, false)
        )
    }

     override fun onBindViewHolder(holder: ViewHolder, position: Int) {
         holder.itemView.address.text = (list[position] as JSONObject).optString("name")
         val address_id=(list[position] as JSONObject).optString("_id")
         holder.itemView.garbage.setOnClickListener {

             Helpers.DeleteAddress.cancel(address_id, context!!)
             list.remove(list[position])
             notifyDataSetChanged()
         }


    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

}


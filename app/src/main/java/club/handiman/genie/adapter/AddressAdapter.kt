package club.handiman.genie.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.genie_cl.R
import kotlinx.android.synthetic.main.feedback.view.*
import kotlinx.android.synthetic.main.radioaddress.view.*
import org.json.JSONObject

class AddressAdapter(var context: Context) : RecyclerView.Adapter<AddressAdapter.ViewHolder>() {

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
                .inflate(R.layout.radioaddress, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.radio_Ad.text = (list[position] as JSONObject).optString("name")



    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

}


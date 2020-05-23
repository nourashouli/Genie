package club.handiman.genie.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import club.handiman.genie.Fragments.ChatLog.ChatLogActivity
import club.handiman.genie.Utils.Utils
import club.handiman.genie.Utils.putExtraJson
import com.bumptech.glide.Glide
import com.example.genie_cl.R
import kotlinx.android.synthetic.main.user_chat_row.view.*
import org.json.JSONArray
import org.json.JSONObject


class ChatAdapter(var context: Context) : RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    var list: ArrayList<Any> = ArrayList()

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

    fun getItems() = list


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.user_chat_row, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//check
        val handyman = (list[position] as JSONObject).getJSONObject("client")
        if (handyman.has("image")) {

            val client_img = handyman.optString("image", "")
            if (client_img == "null") {
                Glide
                    .with(holder.itemView!!)
                    .load(Utils.BASE_IMAGE_URL.plus("services/service_1585417538.png"))
                    .into(holder.itemView.client_msg_image)
            } else {


                Glide
                    .with(holder.itemView!!)
                    .load(Utils.BASE_IMAGE_URL.plus(client_img))
                    .into(holder.itemView.client_msg_image)
            }
        }

        holder.itemView.client_name_msg.text = handyman.optString("name", "name")

        if (!(list[position] as JSONObject).has("messages")) {
            holder.itemView.client_latest_message.text = "Click to send a new message.."
        } else {
            var msgArray: JSONArray = (list[position] as JSONObject).optJSONArray("messages")!!

            holder.itemView.client_latest_message.text =
                msgArray.getJSONObject(msgArray.length() - 1).getString("message")

            val client = (list[position] as JSONObject).getJSONObject("client")
            if (client.has("image")) {

                val handyman_img = client.optString("image", "")
                if (handyman_img == "null") {
                    Glide
                        .with(holder.itemView!!)
                        .load(Utils.BASE_IMAGE_URL.plus("services/service_1585417538.png"))
                        .into(holder.itemView.client_msg_image)
                } else {


                    Glide
                        .with(holder.itemView!!)
                        .load(Utils.BASE_IMAGE_URL.plus(handyman_img))
                        .into(holder.itemView.client_msg_image)
                }
            }

        }
        holder.itemView.setOnClickListener {
            val intent = Intent(context!!, ChatLogActivity::class.java)
            intent.putExtraJson("object", list[position] as JSONObject)
            context!!.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

}


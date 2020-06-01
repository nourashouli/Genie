package club.handiman.genie.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import club.handiman.genie.Fragments.HandymanprofileFragment
import club.handiman.genie.Fragments.PaymentFragment
import club.handiman.genie.Fragments.RequestDetailsFragment
import club.handiman.genie.MainActivity
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
        if ((list[position]).has_receipt == true) {
            if (list[position].has_paid == true) {
                holder.itemView.payment_btn.visibility = View.VISIBLE
                holder.itemView.payment_btn.text="Paid"
                holder.itemView.payment_btn.setBackgroundColor(Color.GREEN)

            } else {
                holder.itemView.payment_btn.visibility = View.VISIBLE
                holder.itemView.payment_btn.setOnClickListener {
                    (context as MainActivity).navigateToFragment(
                        PaymentFragment(
                            (list[position])
                        )
                    )
                }
            }
        }


        holder.itemView.name.text =
            (list[position]).handyman
        holder.itemView.date.text =
            (list[position]).date

        val url = (list[position]).image
        Glide
            .with(holder.itemView)
            .load(Utils.BASE_IMAGE_URL.plus(url))
            .into(holder.itemView.imageView2)

        holder.itemView.cancel.setOnClickListener {

            Helpers.RequestHelper.cancel((list[position]).request_id, context!!)
            list.remove(list[position])
            notifyDataSetChanged()
        }
        holder.itemView.setOnClickListener {

            // {itemClick(layoutPosition)}
            (context as MainActivity).navigateToFragment(
                RequestDetailsFragment(
                    (list[position] as RequestModel)
                )
            )
//           this.listener!!.onAction(list[position])
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

}


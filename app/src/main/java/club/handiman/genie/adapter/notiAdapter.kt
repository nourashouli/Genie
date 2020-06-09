package club.handiman.genie.adapter

import android.R.attr.fragment
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.R
import androidx.core.graphics.toColor
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import club.handiman.genie.Fragments.PaymentFragment
import club.handiman.genie.Fragments.RequestDetailsFragment
import club.handiman.genie.MainActivity
import club.handiman.genie.Models.RequestModel
import club.handiman.genie.Utils.Utils
import com.bumptech.glide.Glide
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
                .inflate(com.example.genie_cl.R.layout.notification_row, parent, false)
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

        holder.itemView.garbage1.setOnClickListener {

            Helpers.RequestHelper.cancel((list[position]).request_id, context!!)
            list.remove(list[position])
            notifyDataSetChanged()
        }
        holder.itemView.setOnClickListener {
            (context as MainActivity).navigateToFragment(RequestDetailsFragment(list[position]))

//           this.listener!!.onAction(list[position])
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

}


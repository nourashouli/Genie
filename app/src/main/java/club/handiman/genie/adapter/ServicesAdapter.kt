package club.handiman.genie.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import club.handiman.genie.Utils.Utils
import com.bumptech.glide.Glide
import com.example.genie_cl.R
import kotlinx.android.synthetic.main.fragment_handymanprofile.*
import kotlinx.android.synthetic.main.service_card.view.*

import org.json.JSONObject

class ServicesAdapter(val context: Context, var dataSource: ArrayList<Any>) : BaseAdapter() {

    private val inflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view: View
        val vh: ItemHolder
        if (convertView == null) {
            view = inflater.inflate(R.layout.address_item, parent, false)
            vh = ItemHolder(view)
            Glide
                .with(context!!)
                .load(Utils.BASE_IMAGE_URL.plus((dataSource.get(position) as JSONObject).optString("image")))
                .into(vh.image)
            view?.tag = vh
        } else {
            view = convertView
            vh = view.tag as ItemHolder
        }
        vh.label.text = (dataSource.get(position) as JSONObject).optString("name")



        return view
    }

    override fun getItem(position: Int): Any? {
        return dataSource[position];
    }

    override fun getCount(): Int {
        return dataSource.size;
    }

    override fun getItemId(position: Int): Long {
        return position.toLong();
    }

    private class ItemHolder(row: View?) {
        val label: TextView
        val image:ImageView


        init {
            label = row?.findViewById(R.id.address_name) as TextView
            image= row?.findViewById(R.id.service_pic) as ImageView
        }
    }

}
package club.handiman.genie.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import club.handiman.genie.Models.RequestModel
import club.handiman.genie.adapter.RequestImagesAdapter
import club.handiman.genie.adapter.imagesAdapter
import com.example.genie_cl.R
import kotlinx.android.synthetic.main.requestdetails.*
import org.json.JSONArray
import org.json.JSONObject


class RequestDetailsFragment(var data: RequestModel) : Fragment() {
    var adapter: imagesAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.requestdetails, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        imagesRecycler.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = imagesAdapter(context!!)

        imagesRecycler.adapter = adapter
        //try

        handymanname1.text = data.handyman
        subject1.text = data.subject
        description1.text = data.request
        date1.text = data.date
        from1.text = data.from
        to1.text =  data.to
//        val items: JSONArray?=data.images
//        for (i in 0 until items!!.length()) {
//            adapter!!.setItem(items.get(i) as RequestModel)
//        }
//        adapter!!.notifyDataSetChanged()


                }
            }



package club.handiman.genie.Fragments

import Helpers.DownloadTask
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import club.handiman.genie.Utils.Utils
import club.handiman.genie.Utils.putExtraJson
import club.handiman.genie.adapter.feedbackAdapter
import club.handiman.genie.requestForm2
import com.bumptech.glide.Glide
import com.example.genie_cl.R
import kotlinx.android.synthetic.main.fragment_handymanprofile.*
import org.json.JSONArray
import org.json.JSONObject


class HandymanprofileFragment(var data: Any, var id: String) : Fragment() {
    var adapter: feedbackAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_handymanprofile, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        adapter = feedbackAdapter(context!!)
        //try
        feedbackrecycler.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        feedbackrecycler.setAdapter(adapter)
        bio.text = (data as JSONObject).getString("biography")
        username.text = (data as JSONObject).getString("name")
        val image_url = (data as JSONObject).optString("image")
        val cv = (data as JSONObject).optString("cv").toString()
        val criminalrecord = (data as JSONObject).optString("criminal_record").toString()
        rBar.rating=(data as JSONObject).optDouble("rating").toFloat()
        val certificatess = (data as JSONObject).optString("certificate").toString()
        if((data as JSONObject).has("feedbacks")){
            val items: JSONArray?= (data as JSONObject).getJSONArray("feedbacks")

            for (i in 0 until items!!.length()) {
                adapter!!.setItem(items.get(i))
            }
            adapter!!.notifyDataSetChanged()

        certificates.setOnClickListener {
            DownloadTask(context!!, "http://www.codeplayon.com/samples/resume.pdf")
        }
        criminal.setOnClickListener {
            DownloadTask(context!!,criminalrecord)
        }

        Cv.setOnClickListener {
            DownloadTask(context!!,cv)
        }
        Glide
            .with(this)
            .load(Utils.BASE_IMAGE_URL.plus(image_url))
            .into(pro_image_profile_frag)
        activity!!.runOnUiThread {
            val ob: JSONObject? = JSONObject()
            ob!!.put("service_id", id!!)
            ob!!.put("employee_id", (data as JSONObject).optString("_id", "id").toString())
            requestbt.setOnClickListener {
                val i = Intent(context!!, requestForm2::class.java)
                i!!.putExtraJson("object", ob)
                startActivity(i)
            }
        }
    }

}}



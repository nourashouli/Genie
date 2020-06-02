package club.handiman.genie.Fragments

import android.app.DownloadManager
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
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
    lateinit internal var uri: Uri
    var adapter: feedbackAdapter? = null
    var certificatess:String="h"
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
         certificatess = (data as JSONObject).optString("certificate").toString()
        val cv = (data as JSONObject).optString("cv").toString()
        val criminalrecord = (data as JSONObject).optString("criminal_record").toString()
        rBar.rating=(data as JSONObject).optJSONObject("rating_object").getDouble(id!!).toFloat()
        if((data as JSONObject).has("feedbacks")){
            val items: JSONArray?= (data as JSONObject).getJSONArray("feedbacks")

            for (i in 0 until items!!.length()) {
                adapter!!.setItem(items.get(i))
            }
            adapter!!.notifyDataSetChanged()

        certificates.setOnClickListener {
           // DownloadTask(context!!, "https://drive.google.com/file/d/0B71LXrqWr0mFUTk5WnVyVEQ3MFE/export?format=pdf")
            StartDownloading(view)
        }
//        criminal.setOnClickListener {
//            DownloadTask(context!!,criminalrecord)
//        }
//
//        Cv.setOnClickListener {
//            DownloadTask(context!!,cv)
//        }
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

}


    fun StartDownloading(view: View?) {

        val title1: String = "nnn"
        DownloadBooks("https://drive.google.com/file/d/0B71LXrqWr0mFUTk5WnVyVEQ3MFE/export?format=pdf", title1)
    }

    fun DownloadBooks(url: String?, title: String) {
        val request = DownloadManager.Request(Uri.parse(url))
        val tempTitle = title.replace("", "_")
        request.setTitle(tempTitle)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request.allowScanningByMediaScanner()
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        }
        request.setDestinationInExternalPublicDir(
            Environment.DIRECTORY_DOWNLOADS,
            "$tempTitle.pdf"
        )
        val downloadManager =
       //   getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager?
         request.setMimeType("application/pdf")
        request.allowScanningByMediaScanner()
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
       //downloadManager!!.enqueue(request)
    }


}


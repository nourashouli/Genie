package com.example.genie_cl.Fragments

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.genie_cl.Utils.Utils
import com.example.genie_cl.R
import com.example.genie_cl.Utils.putExtraJson
import kotlinx.android.synthetic.main.fragment_handymanprofile.*
import org.json.JSONObject
import com.bumptech.glide.Glide
import com.example.genie_cl.MainActivity
import com.example.genie_cl.Utils.putExtraJson
import com.example.genie_cl.adapter.HomeAdapter
import com.example.genie_cl.requestForm2
import com.google.gson.JsonObject



class HandymanprofileFragment(var data: Any, var id: String) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_handymanprofile, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initRecyclerView()

        username.text = (data as JSONObject).getString("name")
        val image_url = (data as JSONObject).optString("image")

        Glide
            .with(this)
            .load(Utils.BASE_IMAGE_URL.plus(image_url))
            .into(pro_image_profile_frag)
        activity!!.runOnUiThread {
            val ob: JSONObject ? = JSONObject()
            ob!!.put("service_id", id!!)
            ob!!.put("employee_id", (data as JSONObject).optString("_id", "id").toString())
            requestbt.setOnClickListener {
                val i = Intent(context!!, requestForm2::class.java)

                i!!.putExtraJson("object", ob)
                startActivity(i)
            } }
//      }



    }

    private fun initRecyclerView(){
        images_recycle_view_id.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        images_recycle_view_id.adapter = HomeAdapter(context as MainActivity, "handyman")
    }

//    private fun handleDisqus(){
//
//        val htmlComments = htmlComment("yourId", "yourShortName")
//
//        val webDisqus = web_view_comment_system_id
//        // set up disqus
//        val webSettings2 = webDisqus.settings
//        webSettings2.javaScriptEnabled = true
//        webSettings2.builtInZoomControls = true
//        webDisqus.requestFocusFromTouch()
//        webDisqus.webViewClient = WebViewClient()
//        webDisqus.webChromeClient = WebChromeClient()
//        webDisqus.loadData(htmlComments, "text/html", null)
//
//    } // handleDisqus

//    private fun htmlComment(idPost: String, shortName: String): String {
//
//        return ("<div id='disqus_thread'></div>"
//                + "<script type='text/javascript'>"
//                + "var disqus_identifier = '"
//                + idPost
//                + "';"
//                + "var disqus_shortname = '"
//                + shortName
//                + "';"
//                + " (function() { var dsq = document.createElement('script'); dsq.type = 'text/javascript'; dsq.async = true;"
//                + "dsq.src = 'http://' + disqus_shortname + '.disqus.com/embed.js';"
//                + "(document.getElementsByTagName('head')[0] || document.getElementsByTagName('body')[0]).appendChild(dsq); })();"
//                + "</script>")

//    } // htmlComment

}

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
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.genie_cl.MainActivity
import com.example.genie_cl.Utils.Utils
import com.example.genie_cl.R
import com.example.genie_cl.Utils.putExtraJson
import com.example.genie_cl.adapter.HomeAdapter
import com.example.genie_cl.requestForm2
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.fragment_handymanprofile.*
import org.json.JSONObject


class HandymanprofileFragment(var data: Any, var id: String) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

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

    private fun initRecyclerView() {
        images_recycle_view_id.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        images_recycle_view_id.adapter = HomeAdapter(context as MainActivity, "handyman")
    }


}
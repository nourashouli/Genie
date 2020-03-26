package com.example.genie_cl.Fragments


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.genie_cl.Utils.Utils
import com.example.genie_cl.adapter.HandymanListAdapter
import com.example.genie_cl.adapter.HomeAdapter
import com.example.genie_cl.requestForm2
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.failure
import com.github.kittinunf.result.success
import kotlinx.android.synthetic.main.fragment_handymanlist.*
import org.json.JSONObject

/**
 * A simple [Fragment] subclass.
 */
class handymanlist(var data: Any) : Fragment() {

    var adapter : HandymanListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(
            com.example.genie_cl.R.layout.fragment_handymanlist,
            container,
            false
        )
       // onCreateView

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity!!.runOnUiThread {
        sort_price.setOnClickListener {
            adapter?.sort("price","asc")

        }}
        adapter =  HandymanListAdapter(context!!)
        handymanlist_recycler.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val id:String= (data as JSONObject).getString("_id")
        handymanlist_recycler.setAdapter(adapter)
//"http://handiman.club/api/getHandymenByService/5e406490278c5d484036f010
        Fuel.get(Utils.API_HANDYMAN_BY_SERVICE.plus(id))
            .header(
                "accept" to "application/json"
            )
            .responseJson { _, _, result ->

                result.success {
                    //
                    var res = it.obj()


                    if (res.optString("status", "error") == "success") {

//                        Toast.makeText(
//                            activity,
//                            res.getJSONArray("handymen").toString(),
//                            Toast.LENGTH_LONG
//                        ).show()
                        activity!!.runOnUiThread {


                            val items = res.getJSONArray("handymen")

                            for (i in 0 until items.length()) {
                                adapter?.setItem(items.getJSONObject(i))
                            }


                        }
                    } else {

                        Toast.makeText(
                            activity,
                            res.getString("status"),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                result.failure {

                    Toast.makeText(activity, it.localizedMessage, Toast.LENGTH_LONG)
                        .show()
                }
            }
//        handymanlist_recycler.setOnClickListener {
//            val i = Intent(this.context, requestForm2::class.java)
//            i.putExtra(id,id)
//            startActivity(i)
//
//
//        }
    }


}

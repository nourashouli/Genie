package com.example.genie_cl.Fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.genie_cl.Utils.Utils
import com.example.genie_cl.adapter.HandymanListAdapter
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.failure
import com.github.kittinunf.result.success
import kotlinx.android.synthetic.main.fragment_search.*

/**
 * A simple [Fragment] subclass.
 */
class handymanlist : Fragment() {

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
    } // onCreateView


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var adapter = HandymanListAdapter(context!!)


        Fuel.get(Utils.API_getHandymanList)
            .header(
                "accept" to "application/json"
            )
            .responseJson { _, _, result ->

                result.success {
                    //
                    var res = it.obj()

                    if (res.optString("status", "error") == "success") {

                        //     var services = res.getJSONObject("services")
                        activity!!.runOnUiThread {


                            val items = res.getJSONArray("HandymanList")

                            for (i in 0 until items.length()) {
                                adapter.setItem(items.getJSONObject(i))
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

        recycler_view.setAdapter(adapter)
    }




}

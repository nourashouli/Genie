package club.handiman.genie.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import club.handiman.genie.Utils.Utils
import club.handiman.genie.adapter.HandymanListAdapter
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
    var adapter: HandymanListAdapter? = null

    var id:String ?=null

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
        activity!!.runOnUiThread {
            sort_price.setOnClickListener {
                adapter?.sort("price", "asc")

            }
        }



        id=(data as JSONObject).getString("_id")
        val adapter = HandymanListAdapter(context!!,id!!)

        handymanlist_recycler.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val id: String = (data as JSONObject).getString("_id")
        handymanlist_recycler.setAdapter(adapter)
        Fuel.get(Utils.API_HANDYMAN_BY_SERVICE.plus(id))
            .header(
                "accept" to "application/json"
            )
            .responseJson { _, _, result ->

                result.success {
                    //
                    var res = it.obj()


                    if (res.optString("status", "error") == "success") {


                        activity!!.runOnUiThread {
//                                Toast.makeText(
//                                    activity,
//                                    res.getJSONArray("handymen").toString(),
//                                    Toast.LENGTH_LONG
//                                ).show()
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

    }


}

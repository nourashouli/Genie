package club.handiman.genie.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import club.handiman.genie.Models.RequestModel
import club.handiman.genie.adapter.notiAdapter
import com.example.genie_cl.R
import club.handiman.genie.Utils.SharedPreferences
import club.handiman.genie.Utils.Utils
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.failure
import com.github.kittinunf.result.success
import kotlinx.android.synthetic.main.fragment_noti.*
import org.json.JSONObject


class OngoingRequestsFragment : Fragment() {
    var adapter: notiAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_noti, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler_view_notifications.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = notiAdapter(context!!)

        recycler_view_notifications.adapter = adapter
        getRequest()
    }

    fun getRequest() {
        Fuel.get(Utils.API_get_ongoing_request)
            .header(
                "accept" to "application/json",
                Utils.AUTHORIZATION to SharedPreferences.getToken(context!!).toString()
            )
            .responseJson { _, _, result ->

                result.success {

                    var res = it.obj()

                    if (res.optString("status", "error") == "success") {
                        activity!!.runOnUiThread {
                            var list = res.getJSONArray("requests")

                            for (i in 0 until list.length()) {
                                var request = list.getJSONObject(i)
                                var handyman = request.optJSONObject("handyman")
                                var service = request.getJSONObject("service")
                                if (!request.has("handyman")) {
                                    handyman = JSONObject()
                                    handyman.put("name", " The engine is searching for a name")
                                    handyman.put("image", "services/service_1585417538.png")
                                }
                                adapter!!.setItem(
                                    RequestModel(
                                        request.optString("_id", "id"),
                                        //try
                                        handyman.optString("name", "w"),
                                        handyman.optString("image", "a"),
                                        request.optString("description"),
                                        false,
                                        false,
                                        request.optString("date"),
                                        request.optString("from"),
                                        request.optString("to"),
                                        request.optString("subject"),
                                        request.optJSONArray("images")
                                    )
                                )


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
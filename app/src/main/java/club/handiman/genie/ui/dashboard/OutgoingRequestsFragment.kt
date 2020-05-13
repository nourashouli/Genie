package club.handiman.genie.ui.dashboard

import androidx.fragment.app.Fragment
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.QuickViewConstants
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RemoteViews
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import club.handiman.genie.Models.RequestModel
import club.handiman.genie.Utils.SharedPreferences
import club.handiman.genie.Utils.Utils
import club.handiman.genie.adapter.notiAdapter
import com.example.genie_cl.R
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.failure
import com.github.kittinunf.result.success
import kotlinx.android.synthetic.main.fragment_noti.*
import kotlinx.android.synthetic.main.outgoing_requests.*
import org.jetbrains.anko.support.v4.runOnUiThread

class OutgoingRequestsFragment : Fragment() {
    var adapter: notiAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.outgoing_requests, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler_outgoing_notifications.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = notiAdapter(context!!)
        getRequests()
        recycler_outgoing_notifications.adapter = adapter
    }

    fun getRequests() {

        Fuel.get(Utils.API_OUTGOING_REQUESTS)
            .header(
                "accept" to "application/json",
                Utils.AUTHORIZATION to SharedPreferences.getToken(context!!).toString()
            )
            .responseJson { _, _, result ->

                result.success {

                    var res = it.obj()

                    if (res.optString("status", "error") == "success") {
                        var list = res.getJSONArray("requests")

                        for (i in 0 until list.length()) {
                            var request = list.getJSONObject(i)
                            var handyman = request.getJSONObject("handyman")
                            var service = request.getJSONObject("service")
                            var flag = false
                            var flag2 = false
                            if (request.has("receipt")) {
                                flag = true
                            }
                            if (request.has("paid")) {
                                flag2 = request.optBoolean("paid")
                            }
                            var model: RequestModel = RequestModel(
                                request.optString("_id", "id"),
                                handyman.optString("name"),
                                handyman.optString("image"), request.optString("description"),
                                flag, flag2, request.optString("date"),
                                request.optString("from"),
                                request.optString("to")
                            )
                            runOnUiThread {
                                adapter!!.setItem(model!!)
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
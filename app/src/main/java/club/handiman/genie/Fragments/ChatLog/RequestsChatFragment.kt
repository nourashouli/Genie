package club.handiman.genie.Fragments.ChatLog

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import club.handiman.genie.Utils.SharedPreferences
import club.handiman.genie.Utils.Utils
import club.handiman.genie.adapter.ChatAdapter
import com.example.genie_cl.R
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.failure
import com.github.kittinunf.result.success
import kotlinx.android.synthetic.main.fragment_requests_chat.*

class RequestsChatFragment : Fragment() {

    private var chat_request: ChatAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(
            R.layout.fragment_requests_chat,
            container,
            false
        )
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chat_request_recycler!!.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)


        chat_request = ChatAdapter(context!!)

        chat_request_recycler.adapter = chat_request
        fetchRequests()
    }

    fun fetchRequests() {
        Fuel.get(Utils.API_CHAT_REQUESTS)
            .header(
                "accept" to "application/json",
                Utils.AUTHORIZATION to SharedPreferences.getToken(activity!!.baseContext).toString()
            )
            .responseJson { _, _, result ->

                result.success {

                    var res = it.obj()

                    if (res.optString("status", "error") == "success") {
                        var list = res.getJSONArray("requests")

                        for (i in 0 until list.length()) {
                            var request = list.getJSONObject(i)
                            activity!!.runOnUiThread {
                                chat_request!!.setItem(request)
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
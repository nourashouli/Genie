package club.handiman.genie.Fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import club.handiman.genie.MainActivity
import club.handiman.genie.Models.RequestModel
import club.handiman.genie.Utils.SharedPreferences
import club.handiman.genie.Utils.Utils

import com.example.genie_cl.R
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.failure
import com.github.kittinunf.result.success
import kotlinx.android.synthetic.main.fragment_reschule_request.*
import org.jetbrains.anko.runOnUiThread

class RescheduleRequest(var item: RequestModel) : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reschule_request, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        rescheduled_date.setText(item.rescheduled_date.toString())
        rescheduled_from.text = item.rescheduled_from.plus(":00")
        rescheduled_to.text = item.rescheduled_to.plus(":00")
        var req_id = item.request_id
        accept_res.setOnClickListener {
            replyToReschedule(req_id!!, "accept")
        }
        reject_res.setOnClickListener {
            replyToReschedule(req_id!!, "rejected")
        }
    }

    fun replyToReschedule(id: String, res: String) {
        Fuel.post(Utils.API_RESCHULE.plus(id!!), listOf("reschedule" to res!!))
            .header("accept" to "application/json",      Utils.AUTHORIZATION to SharedPreferences.getToken(context!!).toString()
            )
            .responseJson { _, _, result ->
                result.success {

                    var res = it.obj()
                    if (res.optString("status", "error") == "success") {

                        val intent = Intent(context!!, MainActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)

                    } else {


                    }
                }
                result.failure {

                    context!!.runOnUiThread {
                        Toast.makeText(context!!, it.localizedMessage, Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
    }
}
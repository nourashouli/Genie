package Helpers

import android.content.Context
import android.widget.Toast
import club.handiman.genie.Models.RequestModel
import club.handiman.genie.Utils.SharedPreferences
import club.handiman.genie.Utils.Utils
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.failure
import com.github.kittinunf.result.success
import org.jetbrains.anko.support.v4.runOnUiThread

class RequestHelper {
    companion object {


        fun cancel(id:String,context:Context) {
            Fuel.post(Utils.API_CANCEL_REQUEST.plus(id))
                .header(
                    "accept" to "application/json",
                    Utils.AUTHORIZATION to SharedPreferences.getToken(context!!).toString()
                )
                .responseJson { _, _, result ->

                    result.success {

                        var res = it.obj()

                        if (res.optString("status", "error") == "success") {
                          // it does not return anything

                        } else {

                            Toast.makeText(
                                context!!,
                                res.getString("status"),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                    result.failure {

                        Toast.makeText(context!!, it.localizedMessage, Toast.LENGTH_LONG)
                            .show()
                    }
                }
        }
    }
}
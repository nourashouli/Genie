package Helpers

import android.content.Context
import android.widget.Toast
import club.handiman.genie.Utils.SharedPreferences
import club.handiman.genie.Utils.Utils
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.failure
import com.github.kittinunf.result.success
import com.google.gson.JsonObject
import org.jetbrains.anko.support.v4.runOnUiThread
import org.json.JSONObject

class EmployeeProfile {
    companion object {
        fun  employeeprofile(id:String,context:Context): Any? {
            var res: JSONObject? =null
            Fuel.get(Utils.API_EMPLOYEE_PROFILE.plus(id))
                    .header(
                            "accept" to "application/json",
                            Utils.AUTHORIZATION to SharedPreferences.getToken(context!!).toString()
                )
                .responseJson { _, _, result ->

                    result.success {

                 res = it.obj()

                if (res!!.optString("status", "error") == "success") {
                    // it does not return anything

                } else {

                    Toast.makeText(
                            context!!,
                            res!!.getString("status"),
                            Toast.LENGTH_LONG
                            ).show()
                }
            }
                result.failure {

                    Toast.makeText(context!!, it.localizedMessage, Toast.LENGTH_LONG)
                            .show()
                }
            }

            return res
        }
    }
}
package club.handiman.genie

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import club.handiman.genie.Utils.SharedPreferences
import club.handiman.genie.Utils.Utils
import com.example.genie_cl.R
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.failure
import com.github.kittinunf.result.success
import kotlinx.android.synthetic.main.activity_rating2.*
import org.json.JSONObject

class Rating : AppCompatActivity() {
    var request_id: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rating2)
//
        var obje: JSONObject = JSONObject(intent!!.extras!!.getString("object"))
        var object2 = JSONObject(obje.getString("nameValuePairs"))
        request_id = (object2).optString("request_id")

        submit?.setOnClickListener {
            val rate = rBar.rating.toString()
            val feedback = review.text.toString()
            Fuel.post(
                Utils.API_REQUEST_Done.plus(request_id!!), listOf(
                    "rating" to rate,
                    "feedback" to feedback

                )
            ).header(
                "accept" to "application/json",
                Utils.AUTHORIZATION to SharedPreferences.getToken(this).toString()
            )
                .responseJson { _, _, result ->
                    result.success {
                        runOnUiThread {
                            val intent = Intent(this, MainActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)

                            startActivity(intent)
                        }
                    }
                    result.failure {
                        runOnUiThread {
                            Toast.makeText(
                                this,
                                it.localizedMessage.toString()!!,
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }

                }
        }
    }
}

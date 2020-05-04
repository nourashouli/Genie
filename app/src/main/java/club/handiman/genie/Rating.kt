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

class Rating : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rating2)
        submit?.setOnClickListener {
            val rate = rBar.rating.toString()
            Toast.makeText(
                this@Rating,
                "Rating is: " + rate, Toast.LENGTH_SHORT
            ).show()
            val feedback = review.text.toString()
            Fuel.post(
                Utils.API_REQUEST_Done, listOf(
                    "rating" to rate,
                    "feedback" to feedback

                )
            ).header(
                "accept" to "application/json",
                Utils.AUTHORIZATION to SharedPreferences.getToken(this).toString()
            )
                .responseJson() { _, _, result ->
                    result.success {

                    }
                    result.failure {
                        runOnUiThread {
                            Toast.makeText(this, it.localizedMessage.toString()!!, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
        }
        }
    }

package club.handiman.genie.Fragments

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import android.widget.Toast
import club.handiman.genie.Models.RequestModel
import club.handiman.genie.Rating
import club.handiman.genie.Utils.SharedPreferences
import club.handiman.genie.Utils.Utils
import club.handiman.genie.Utils.putExtraJson
import com.example.genie_cl.R
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.failure
import com.github.kittinunf.result.success
import com.stripe.android.ApiResultCallback
import com.stripe.android.PaymentConfiguration
import com.stripe.android.Stripe
import com.stripe.android.model.Card
import com.stripe.android.model.Token
import kotlinx.android.synthetic.main.fragment_payment.*
import org.jetbrains.anko.support.v4.runOnUiThread
import org.json.JSONObject
import java.lang.ref.WeakReference


class PaymentFragment(var ob: Any) : Fragment() {


    lateinit var stripe: Stripe
    val publishKey: String = "pk_test_Yzk4eIQ2VOEGQFZ70vFBuQur00xW3XqfFv"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_payment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        submit_payment.setOnClickListener {
            validateCard()
        }
    }

    fun sendTokenToServer(token: String) {
        val request_id = (ob as RequestModel).request_id
        val intent = Intent(context!!, Rating::class.java)
        intent.flags =
            Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)

        val ob: JSONObject? = JSONObject()
        ob!!.put("request_id", request_id)
        intent!!.putExtraJson("object", ob)
        Fuel.post(
            Utils.API_CONFIRM_PAYMENT.plus(request_id), listOf(
                "stripe_token" to token
            )
        ).header(
            "accept" to "application/json",
            Utils.AUTHORIZATION to SharedPreferences.getToken(context!!).toString()
        )
            .responseJson { _, _, result ->
                result.success {
                    runOnUiThread {


                        startActivity(intent)
                    }
                }
                result.failure {
                    runOnUiThread {
                        Toast.makeText(
                            context!!,
                            it.localizedMessage!!.toString(),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }

            }
    }

    fun validateCard() {
        Toast.makeText(context!!, "hello", Toast.LENGTH_LONG).show()
        cardInputWidget.card?.let { card ->
            if (!card.validateNumber() || !card.validateCVC()
                || !card.validateExpiryDate()
            ) {
                runOnUiThread {
                    Toast.makeText(context!!, card.toString(), Toast.LENGTH_LONG).show()

                }

            }
        }
        PaymentConfiguration.init(context!!, publishKey!!)
//                PaymentConfiguration.init(applicationContext, "")
        var stripe = Stripe(context!!, PaymentConfiguration.getInstance(context!!).publishableKey)
        stripe.createCardToken(cardInputWidget.card!!, null, object : ApiResultCallback<Token> {
            override fun onSuccess(result: Token) {
                val tokenID = result.id
                sendTokenToServer(tokenID)
            }

            override fun onError(e: java.lang.Exception) {
                // Handle error
                runOnUiThread {

                    Toast.makeText(context!!, e.localizedMessage, Toast.LENGTH_LONG).show()
                }

            }
        })
    }
}

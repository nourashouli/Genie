package club.handiman.genie
import android.app.Activity
import android.content.Intent
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.genie_cl.R
import android.content.DialogInterface;
import android.os.Bundle;
import android.telecom.Call
import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.fuel.core.isSuccessful
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentConfiguration
import com.stripe.android.PaymentIntentResult;
import com.stripe.android.Stripe;
import com.stripe.android.model.ConfirmPaymentIntentParams;
import java.lang.ref.WeakReference;
import com.stripe.android.model.StripeIntent
import kotlinx.android.synthetic.main.activity_checkout.*
import org.json.JSONObject
import java.io.IOException
import javax.security.auth.callback.Callback


class CheckoutActivity : AppCompatActivity() {
    private val backendUrl = "http://10.0.2.2:4242/"
   // private val httpClient = OkHttpClient()

    private fun displayAlert(activity: Activity?, title: String, message: String, restartDemo: Boolean = false) {
        // omitted for brevity
    }

    private fun pay(paymentMethod: String?, paymentIntent: String?) {
        val weakActivity = WeakReference<Activity>(this)
        var json = ""
        if (!paymentMethod.isNullOrEmpty()) {
            json = """
                {
                    "useStripeSdk":true,
                    "paymentMethodId":"$paymentMethod",
                    "currency":"usd",
                    "items": [
                        {"id":"photo_subscription"}
                    ]
                }
                """
        }
        else if (!paymentIntent.isNullOrEmpty()) {
            json = """
                {
                    "paymentIntentId":"$paymentIntent"
                }
                """
        }
        // Create a PaymentIntent on the server
//        val mediaType = "application/json; charset=utf-8".toMediaType()
//        val body = json.toRequestBody(mediaType)
//        val request = Request.Builder()
//            .url(backendUrl + "pay")
//            .post(body)
//            .build()
//        httpClient.newCall(request)
//            .enqueue(object: Callback {
//                override fun onFailure(call: Call, e: IOException) {
//                    displayAlert(weakActivity.get(), "Payment failed", "Error: $e")
//                }
//
//                override fun onResponse(call: Call, response: Response) {
//                    // Request failed
//                    if (!response.isSuccessful) {
//                        displayAlert(weakActivity.get(), "Payment failed", "Error: $response")
//                    } else {
//                        val responseData = response.body?.toString()
//                        var responseJson = JSONObject(responseData)
//                        val payError: String? = responseJson.optString("error")
//                        val clientSecret: String? = responseJson.optString("clientSecret")
//                        val requiresAction: Boolean? = responseJson.optBoolean("requiresAction")
//                        // Payment failed
//                        if (payError != null && payError.isNotEmpty()) {
//                            displayAlert(weakActivity.get(), "Payment failed", "Error: $payError")
//                        }
//                        // Payment succeeded
//                        else if ((clientSecret != null && clientSecret.isNotEmpty())
//                            && (requiresAction == null || requiresAction == false)) {
//                            displayAlert(weakActivity.get(), "Payment succeeded", "$clientSecret", restartDemo = true)
//                        }
//                        // Payment requires additional actions
//                        else if ((clientSecret != null && clientSecret.isNotEmpty())
//                            && requiresAction == true) {
//                            runOnUiThread {
//                                if (weakActivity.get() != null) {
//                                    // ...continued in the next step
//                                }
//                            }
//                        }
//                    }
//                }
//            })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val weakActivity = WeakReference<Activity>(this)

        // Handle the result of stripe.authenticatePayment
//        stripe.onPaymentResult(requestCode, data, object : ApiResultCallback<PaymentIntentResult> {
//            override fun onSuccess(result: PaymentIntentResult) {
//                val paymentIntent = result.intent
//                val status = paymentIntent.status
//                if (status == StripeIntent.Status.Succeeded) {
//                    val gson = GsonBuilder().setPrettyPrinting().create()
//                    displayAlert(weakActivity.get(), "Payment succeeded", gson.toJson(paymentIntent), restartDemo = true)
//                } else if (status == StripeIntent.Status.RequiresPaymentMethod) {
//                    // Payment failed – allow retrying using a different payment method
//                    displayAlert(weakActivity.get(), "Payment failed", paymentIntent.lastPaymentError!!.message ?: "")
//                }
//                else if (status == StripeIntent.Status.RequiresConfirmation) {
//                    print("Re-confirming PaymentIntent after handling a required action")
//                    pay(null, paymentIntent.id)
//                }
//                else {
//                    displayAlert(weakActivity.get(), "Payment status unknown", "unhandled status: $status", restartDemo = true)
//                }
//            }
//
//            override fun onError(e: Exception) {
//                // Payment request failed – allow retrying using the same payment method
//                displayAlert(weakActivity.get(), "Payment failed", e.toString())
//            }
//        })
   }
}

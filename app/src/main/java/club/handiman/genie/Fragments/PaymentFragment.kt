package club.handiman.genie.Fragments

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.TypedValue
import android.view.*
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import club.handiman.genie.MainActivity
import club.handiman.genie.Models.RequestModel
import club.handiman.genie.Rating
import club.handiman.genie.Utils.SharedPreferences
import club.handiman.genie.Utils.Utils
import club.handiman.genie.Utils.putExtraJson
import club.handiman.genie.adapter.ReceiptAdapter
import club.handiman.genie.adapter.ReceiptImagesAdapter
import club.handiman.genie.adapter.imagesAdapter
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
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.report_dialog.*
import org.jetbrains.anko.support.v4.runOnUiThread
import org.json.JSONObject
import java.lang.ref.WeakReference


class PaymentFragment(var ob: Any) : Fragment() {

    val request_id = (ob as RequestModel).request_id
    lateinit var stripe: Stripe
    val publishKey: String = "pk_test_Yzk4eIQ2VOEGQFZ70vFBuQur00xW3XqfFv"
    var report_content="f"
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
        report.setOnClickListener {
          showDialog()

        }

        total_amount.text = (ob as RequestModel).total.toString().plus(" $")
        val receipt = (ob as RequestModel).receipt
        receipt_recyler!!.layoutManager =
            LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
        var receiptAdap = ReceiptAdapter(context!!)
        for (i in 0 until receipt!!.length()) {
            receiptAdap.setItem(receipt.getJSONObject(i))
        }
        receipt_recyler.adapter = receiptAdap


        val images = (ob as RequestModel).receipt_images

        var receiptImages = ReceiptImagesAdapter(context!!)
        for (i in 0 until images!!.length()) {
            receiptImages.setItem(images.getString(i))
        }

        val mLayoutManager = GridLayoutManager(context!!, 2)
        receipt_images.setLayoutManager(mLayoutManager)
        receipt_images.addItemDecoration(GridSpacingItemDecoration(2, dpToPx(10), true))
        receipt_images.setItemAnimator(DefaultItemAnimator())
        receipt_images.setAdapter(receiptImages)


        receipt_images.setLayoutManager(mLayoutManager)
        receipt_images.addItemDecoration(GridSpacingItemDecoration(2, dpToPx(10), true))
        receipt_images.setItemAnimator(DefaultItemAnimator())


    }
    private fun showDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.report_dialog)
        val save = dialog.findViewById(R.id.save) as TextView
        val cancel = dialog.findViewById(R.id.cancel) as TextView
        save.setOnClickListener {
            dialog.dismiss()
            report_content = dialog.reportt.text.toString()

        }
        cancel.setOnClickListener {
            dialog.dismiss()

        }
        dialog.show()
    }

    fun sendTokenToServer(token: String) {

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

    inner class GridSpacingItemDecoration(
        private val spanCount: Int,
        private val spacing: Int,
        private val includeEdge: Boolean
    ) : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val position = parent.getChildAdapterPosition(view) // item position
            val column = position % spanCount // item column

            if (includeEdge) {
                outRect.left =
                    spacing - column * spacing / spanCount // spacing - column * ((1f / spanCount) * spacing)
                outRect.right =
                    (column) * spacing / spanCount // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing
                }
                outRect.bottom = spacing // item bottom
            } else {
                outRect.left = column * spacing / spanCount // column * ((1f / spanCount) * spacing)
                outRect.right =
                    spacing - (column) * spacing / spanCount // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private fun dpToPx(dp: Int): Int {
        val r = resources
        return Math.round(
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp.toFloat(),
                r.displayMetrics
            )
        )
    }
    fun reject_payment(){
        Fuel.post(
            Utils.API_REJECT_PAYMENT.plus(request_id), listOf(
                "report" to report_content

            )

        ).header(
            "accept" to "application/json",
            Utils.AUTHORIZATION to SharedPreferences.getToken(requireContext()).toString()
        ).responseJson { _, _, result ->

            result.success {

                    Toast.makeText(requireContext(), "your report has been sent successfully waiting for an action from the admin", Toast.LENGTH_LONG)
                        .show()

                    (context as MainActivity).navigateToFragment(HomeFragment())
                }

            result.failure {
                Toast.makeText(requireContext(), it.localizedMessage, Toast.LENGTH_LONG)
                    .show()
            }

        }


    }

}



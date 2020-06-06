package club.handiman.genie.Fragments

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Parcel
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import club.handiman.genie.AccountSettingActivity
import club.handiman.genie.MainActivity
import com.example.genie_cl.R
import club.handiman.genie.Utils.Utils
import kotlinx.android.synthetic.main.fragment_profile.*
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.failure
import com.bumptech.glide.Glide
import com.github.kittinunf.result.success
import club.handiman.genie.Utils.SharedPreferences
import club.handiman.genie.adapter.locationAdapter
import club.handiman.genie.requestForm
import com.google.android.libraries.places.api.model.Place
import com.rtchagas.pingplacepicker.PingPlacePicker
import kotlinx.android.synthetic.main.view.*
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.toast
import org.json.JSONArray
import org.json.JSONObject


/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {
    var title = "d"
    var building = "d"
    var zip = "d"
    var floor = "d"
    var street = "d"
    var adapter: locationAdapter? = null
    private val pingActivityRequestCode = 1001
    var location = DoubleArray(2)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        adapter = locationAdapter(requireContext())
        //try
        locations.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        locations.setAdapter(adapter)
        viewProfile()
        edit_account_settings_btn.setOnClickListener {
            (context as MainActivity).navigateToFragment(AccountSettingActivity())

        }
        Logout.setOnClickListener {
            Utils.logout(requireContext())

        }
        newAddress.setOnClickListener {
            showPlacePicker()
        }
    }

    private fun showPlacePicker() {
        val builder = PingPlacePicker.IntentBuilder()
        builder.setAndroidApiKey(getString(R.string.android))
            .setMapsApiKey(getString(R.string.maps))

        try {
            val placeIntent = builder.build(context!! as Activity)
            startActivityForResult(placeIntent, pingActivityRequestCode)
        } catch (ex: Exception) {
            toast("Google Play Services is not Available")
        }
    }

    override fun onActivityResult(
        requestCode: Int, resultCode: Int,
        data: Intent?
    ) {
        showDialog()
        if ((requestCode == pingActivityRequestCode) && (resultCode == Activity.RESULT_OK)) {

            val place: Place? = PingPlacePicker.getPlace(data!!)
            toast("You selected: ${place!!.name}")
            location[0] = place!!.latLng!!.latitude!!.toDouble()
            location[1] = place!!.latLng!!.longitude!!.toDouble()

        }
    }

    private fun showDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.view)
        val noBtn = dialog.findViewById(R.id.nobtn) as TextView
        noBtn.setOnClickListener {
            dialog.dismiss()
            title = dialog.titlee.text.toString()
            building = dialog.building.text.toString()
            zip = dialog.zip.text.toString()
            floor = dialog.floor.text.toString()
            street = dialog.street.text.toString()
            saveProfile()
        }
        dialog.show()
    }

    private fun viewProfile() {
        Fuel.get(Utils.API_EDIT_PROFILE)
            .header(
                "accept" to "application/json",
                Utils.AUTHORIZATION to SharedPreferences.getToken(requireActivity().baseContext).toString()
            )
            .responseJson { _, _, result ->

                result.success {

                    var res = it.obj()

                    if (res.optString("status", "error") == "success") {

                        var profile = res.getJSONObject("profile")
                        requireActivity().runOnUiThread {

                            full_name_profile_frag.setText(profile.optString("name", ""))
                            biography.setText(profile.optString("biography", ""))

                            val url =
                                Utils.BASE_IMAGE_URL.plus(profile.optString("image", "ic_user.png"))

                            Glide
                                .with(this)
                                .load(url).into(pro_image_profile_frag)
                            if (profile.has("client_addresses")) {
                                val items: JSONArray? = profile.getJSONArray("client_addresses")

                                for (i in 0 until items!!.length()) {
                                    adapter!!.setItem(items.getJSONObject(i))
                                }
                                adapter!!.notifyDataSetChanged()
                            }
                        }

                    } else {

                        Toast.makeText(
                            activity,
                            res.getString("status"),
                            Toast.LENGTH_LONG!!
                        ).show()
                    }
                }
                result.failure {

                    Toast.makeText(activity, it.localizedMessage, Toast.LENGTH_LONG)
                        .show()
                }
            }


    }

    private fun saveProfile() {
        Fuel.post(
            Utils.API_EDIT_PROFILE, listOf(
                "address" to true,
                "name" to title,
                "street" to street,
                "zip" to zip,
                "floor" to floor,
                "building" to building
                , "lat" to location[0],
                "lng" to location[1]
            )

        ).header(
            "accept" to "application/json",
            Utils.AUTHORIZATION to SharedPreferences.getToken(requireContext()).toString()
        ).responseJson { _, _, result ->

            result.success {

                var res = it.obj()
                if (res.optString("status", "error") == "success") {

                    var profile = res.getJSONObject("user")

                }
            }
            result.failure {
                Toast.makeText(requireContext(), it.localizedMessage, Toast.LENGTH_LONG)
                    .show()
            }

        }


    }
}


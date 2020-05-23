package club.handiman.genie.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import club.handiman.genie.AccountSettingActivity
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
import org.json.JSONArray

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {
    var adapter: locationAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    adapter = locationAdapter(context!!)
        //try
        locations.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        locations.setAdapter(adapter)
viewProfile()
        edit_account_settings_btn.setOnClickListener {
            val i = Intent(this.context, AccountSettingActivity::class.java)
            startActivity(i)

        }
        Logout.setOnClickListener {
           Utils.logout(context!!)

        }
    }

    private fun viewProfile() {
        Fuel.get(Utils.API_EDIT_PROFILE)
            .header(
                "accept" to "application/json",
                Utils.AUTHORIZATION to SharedPreferences.getToken(activity!!.baseContext).toString()
            )
            .responseJson { _, _, result ->

                result.success {

                    var res = it.obj()

                    if (res.optString("status", "error") == "success") {

                        var profile = res.getJSONObject("profile")
                        activity!!.runOnUiThread {

                            full_name_profile_frag.setText(profile.optString("name", ""))
                            biography.setText(profile.optString("biography", ""))

                            val url =
                                Utils.BASE_IMAGE_URL.plus(profile.optString("image", "ic_user.png"))

                            Glide
                                .with(this)
                                .load(url).into(pro_image_profile_frag)

                            val items:JSONArray ?= profile.optJSONArray("client_addresses")

                            for (i in 0 until items!!.length()) {
                                adapter!!.setItem(items.getJSONObject(i))
                            }
                        adapter!!.notifyDataSetChanged()
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
}

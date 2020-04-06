package com.example.genie_cl.Fragments

import android.text.InputType
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.genie_cl.AccountSettingActivity
import com.example.genie_cl.R
import com.example.genie_cl.Utils.Utils
import kotlinx.android.synthetic.main.fragment_profile.*
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.failure
import com.bumptech.glide.Glide
import com.github.kittinunf.result.success
import com.example.genie_cl.Utils.SharedPreferences

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
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
//                            }
                        }
                    } else {

                        Toast.makeText(
                            activity,
                            res.getString("status"),
                            Toast.LENGTH_LONG
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

package com.example.genie_cl
import com.example.genie_cl.R
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.genie_cl.Fragments.ProfileFragment
import com.example.genie_cl.Utils.SharedPreferences
import com.example.genie_cl.Utils.Utils
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.failure
import com.github.kittinunf.result.success
import kotlinx.android.synthetic.main.activity_account_settings.*
import androidx.fragment.app.Fragment
class AccountSettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_settings)
//        Glide
//            .with(this@AccountSettingActivity)
//            .load("https://handiman.club/public/storage/uploads/dzYci2r374tKkI7NdBtNu3L5K.png")
//            .into(profile_image_edt_profile)
        viewProfile()
        save_infor_profile_btn.setOnClickListener { EditProfile() }
    }

    private fun viewProfile() {
        Fuel.get(Utils.API_EDIT_PROFILE)
            .header(
                "accept" to "application/json",
                Utils.AUTHORIZATION to SharedPreferences.getToken(this).toString()
            )
            .responseJson { _, _, result ->
                result.success {
                    var res = it.obj()
                    if (res.optString("status", "error") == "success") {
                        var profile = res.getJSONObject("profile")
                        email_edt_profile.setText(profile.optString("email", "emailer"))
                        password_edt_profile.setText(profile.optString("password", ".."))
                        runOnUiThread {
                            if (profile.has("profile_picture")) {
                                val url =
                                    Utils.BASE_IMAGE_URL.plus(profile.getString("profile_picture"))
                                Glide
                                    .with(this@AccountSettingActivity)
                                    .load(url)
                                    .into(profile_image_edt_profile)
                            }
                        }


                    } else {

                        Toast.makeText(
                            this,

                            res.getString("status"),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }





                result.failure {

                    Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG)
                        .show()
                }
            }

    }
    private fun EditProfile() {
        Fuel.put(Utils.API_EDIT_PROFILE)
            .header(
                "accept" to "application/json",
                Utils.AUTHORIZATION to SharedPreferences.getToken(this).toString()
            )
            .responseJson { _, _, result ->
                result.success {
                    var res = it.obj()
                    if (res.optString("status", "error") == "success") {
                        var profile = res.getJSONObject("profile")
                        email_edt_profile.setText(profile.optString("email", "emailer"))
                        password_edt_profile.setText(profile.optString("password", ".."))
                        bio.setText(profile.optString("bio"))
                        name_edt_profile.setText(profile.optString("name"))
                        location.setText(profile.optString("location"))
                        save_infor_profile_btn.isEnabled = false
                        Fuel.post(
                                Utils.API_Register, listOf(
                                    "password_confirmation" to confirm_edt_profile,
                                    "name" to name_edt_profile,
                                    "email" to email_edt_profile,
                                    "password" to password_edt_profile
                                    ,
                                    "bio" to bio,
                                    "location" to location

                                )
                            )
                            .header("accept" to "application/json")
                            .responseJson { _, _, result ->
                                result.success {

                                    var res = it.obj()
                                    if (res.optString("status", "error") == "success") {

                                        //  Toast.makeText(this, "Success.", Toast.LENGTH_SHORT).show()

                                        var user = res.getJSONObject("user")

                                        runOnUiThread {
                                            if (profile.has("profile_picture")) {
                                                val url =
                                                    Utils.BASE_IMAGE_URL.plus(profile.getString("profile_picture"))
                                                Glide
                                                    .with(this@AccountSettingActivity)
                                                    .load(url)
                                                    .into(profile_image_edt_profile)
                                            }
                                        }


                                    } else {

                                        Toast.makeText(
                                            this,

                                            res.getString("status"),
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }





                                result.failure {

                                    Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG)
                                        .show()
                                }
                            }

                    }


                }}}}
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
import android.provider.MediaStore
import com.github.kittinunf.result.failure
import com.github.kittinunf.result.success
import kotlinx.android.synthetic.main.activity_account_settings.*
import androidx.fragment.app.Fragment
import android.net.Uri
class AccountSettingActivity : AppCompatActivity() {
    private var image: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_settings)

        viewProfile()
        save_infor_profile_btn.setOnClickListener { editProfile() }
        change_image_text_btn.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
    }


    private fun editProfile() {
        var bio = biography.text.toString()
        var name=name_edt_profile.text.toString()
        var email=email_edt_profile.text.toString()
        var password=password_edt_profile.text.toString()
        Fuel.post(
            Utils.API_EDIT_PROFILE, listOf(
                "image" to image,
                "biography" to bio,"name" to name,"email" to email,"password" to password)

        ).header(
            "accept" to "application/json",
            Utils.AUTHORIZATION to SharedPreferences.getToken(baseContext).toString()
        ).responseJson { _, _, result ->

            result.success {

                var res = it.obj()
                if (res.optString("status", "error") == "success") {

                    var profile = res.getJSONObject("user")

                    }
                }
                result.failure {
                    Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG)
                        .show()
                }

            }
        val i = Intent(this,ProfileFragment::class.java)
        startActivity(i)
        }
    var selectedPhotoUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode ==RESULT_OK && data != null) {
            selectedPhotoUri = data.data
            val bitmap =
                MediaStore.Images.Media.getBitmap(this.contentResolver, selectedPhotoUri)
            profilei.setImageBitmap(bitmap)
            change_image_text_btn.alpha = 0f
            image= Utils.encodeToBase64(bitmap)

        }
    }

    private fun viewProfile() {
        Fuel.get(Utils.API_EDIT_PROFILE)
            .header(
                "accept" to "application/json",
                Utils.AUTHORIZATION to SharedPreferences.getToken(baseContext).toString()
            )
            .responseJson { _, _, result ->

                result.success {

                    var res = it.obj()

                    if (res.optString("status", "error") == "success") {

                        var profile = res.getJSONObject("profile")
                        runOnUiThread {

                            name_edt_profile.setText(profile.optString("name", ""))
                            biography.setText(profile.optString("biography", ""))
                            email_edt_profile.setText(profile.optString("email", ""))
                            password_edt_profile.setText(profile.optString("password", ""))
                            }

                            val url =
                                Utils.BASE_IMAGE_URL.plus(profile.optString("image", ""))

                            Glide
                                .with(this)
                                .load(url).into(profilei)

//                            runOnUiThread {
//                                Toast.makeText(this, profile.toString(), Toast.LENGTH_LONG)
//                                    .show()
//                            }
                        }
                     else {

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
}
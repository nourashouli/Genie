package club.handiman.genie
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import club.handiman.genie.Fragments.ProfileFragment
import com.bumptech.glide.Glide
import club.handiman.genie.Utils.SharedPreferences
import club.handiman.genie.Utils.Utils
import com.example.genie_cl.R
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.failure
import com.github.kittinunf.result.success
import kotlinx.android.synthetic.main.activity_account_settings.*
import org.jetbrains.anko.support.v4.runOnUiThread

class AccountSettingActivity() : Fragment() {
    private var image: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_account_settings, container, false)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewProfile()
        save_infor_profile_btn.setOnClickListener {
            var password=password_edt_profile.text.toString()
            if (password.count()>=8){
            changepass()}
            else if (password.count()<8 && password.isNotEmpty())  {Toast.makeText(requireContext(), "password should be minimum 8 characters", Toast.LENGTH_LONG)
                .show()}
            editProfile()
            }
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
        Fuel.post(
            Utils.API_EDIT_PROFILE, listOf(
                "image" to image,
                "biography" to bio,"name" to name,"email" to email)

        ).header(
            "accept" to "application/json",
            Utils.AUTHORIZATION to SharedPreferences.getToken(requireContext()).toString()
        ).responseJson { _, _, result ->

            result.success {

                var res = it.obj()
                if (res.optString("status", "error") == "success") {

                    var profile = res.getJSONObject("user")
                    (context as MainActivity).navigateToFragment(ProfileFragment())

                    }

                }
                result.failure {
                    Toast.makeText(requireContext(), it.localizedMessage, Toast.LENGTH_LONG)
                        .show()
                }

            }
    }
    private fun changepass() {

        var password=password_edt_profile.text.toString()
        Fuel.post(
            Utils.API_CHANGE_PASSWORD, listOf(
            "password" to password)

        ).header(
            "accept" to "application/json",
            Utils.AUTHORIZATION to SharedPreferences.getToken(requireContext()).toString()
        ).responseJson { _, _, result ->

            result.success {

                var res = it.obj()
                if (res.optString("status", "error") == "success") {
                }
            }
            result.failure {
                Toast.makeText(requireContext(), it.localizedMessage, Toast.LENGTH_LONG)
                    .show()
            }

        }
    }
    var selectedPhotoUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            selectedPhotoUri = data.data
            val bitmap =
                MediaStore.Images.Media.getBitmap(requireContext().contentResolver, selectedPhotoUri)
            profilei.setImageBitmap(bitmap)
            change_image_text_btn.alpha = 0f
            image= Utils.encodeToBase64(bitmap)

        }
    }

    private fun viewProfile() {
        Fuel.get(Utils.API_EDIT_PROFILE)
            .header(
                "accept" to "application/json",
                Utils.AUTHORIZATION to SharedPreferences.getToken(requireContext()).toString()
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
                            requireContext(),
                            res.getString("status"),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                result.failure {

                    Toast.makeText(requireContext(), it.localizedMessage, Toast.LENGTH_LONG)
                        .show()
                }
            }


    }
}
package com.example.genie_cl
import android.view.View
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.genie_cl.Utils.Constants
import com.example.genie_cl.Utils.SharedPreferences
import com.example.genie_cl.Utils.Utils
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.failure
import com.github.kittinunf.result.success
import kotlinx.android.synthetic.main.fragment_registration.*

import android.util.Log
import java.util.*
import android.provider.MediaStore
import com.google.firebase.auth.FirebaseAuth
import com.example.genie_cl.Models.User

class SignUp : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_registration)

        fbtn_register.setOnClickListener{register()}
        //fbtn_register.setOnClickListener{performRegister()}
        sign_in_button.setOnClickListener{login()}
        }
    fun register(){
        val email=email_reg.text.toString()
        val name=name.text.toString()
        val password = reg_password.text.toString()
        val passwordConfirmation=confirmtionpassword.text.toString()
        val role="user"
        fbtn_register.isEnabled=false
        Fuel.post(
            Utils.API_Register,  listOf(
                "password_confirmation"    to passwordConfirmation, "role" to role,
                "name" to name, "email" to email, "password" to password

            )
        )
            .header("accept" to "application/json")
            .responseJson { _, _, result ->
                result.success {

                    var res = it.obj()
                    if (res.optString("status", "error") == "success") {

                        //  Toast.makeText(this, "Success.", Toast.LENGTH_SHORT).show()

                        var user = res.getJSONObject("user")

                        SharedPreferences.setPrefernces(
                            this@SignUp, Constants.FILE_USER,
                            Constants.USER_EMAIL, user.getString("email")
                        )
                        SharedPreferences.setPrefernces(
                            this@SignUp, Constants.FILE_USER,
                            Constants.USER_NAME, user.getString("name")
                        )
                        SharedPreferences.setPrefernces(
                            this@SignUp, Constants.FILE_USER,
                            Constants.USER_TOKEN, res.getString("token")
                        )
                        runOnUiThread {
                            Toast.makeText(
                                this,
                                SharedPreferences.getToken(this@SignUp).toString(),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        val intent = Intent(this, MainActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)


//                       SharedPreferences.clearPreferences(this@MainActivity, Constants.FILE_USER)
                    } else {
                        Toast.makeText(
                            this@SignUp,
                            res.getString("message"),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                result.failure {
                    runOnUiThread {
                        Toast.makeText(this@SignUp, it.localizedMessage, Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
    }

    fun openMainActivity(view: View) {
        val intent = Intent(this@SignUp, MainActivity::class.java)

        intent.flags =
            Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
    fun login() {
        val intent = Intent(this@SignUp, Login::class.java)

        intent.flags =
            Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}

package com.example.genie_cl

import android.content.Context
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
import kotlinx.android.synthetic.main.fragment_login.*
import android.util.Log
import com.google.firebase.auth.FirebaseAuth

import com.google.android.libraries.places.api.model.Place
import com.rtchagas.pingplacepicker.PingPlacePicker

class Login : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_login)

        if (SharedPreferences.getToken(this@Login) != null) {
            startActivity(Intent(this@Login, MainActivity::class.java))
            finish()
        }
        btn_login.setOnClickListener { login() }
        registration_id.setOnClickListener { Signup() }
        // btn_login.setOnClickListener { performLogin() }
    }


    private fun login() {
        btn_login.isEnabled = false
        val email = Email.text.toString()
        val password = Password.text.toString()
        val role = "user"
        Fuel.post(Utils.API_LOGIN, listOf("email" to email, "password" to password, "role" to role))
            .header("accept" to "application/json")
            .responseJson { _, _, result ->
                result.success {

                    var res = it.obj()
                    if (res.optString("status", "error") == "success") {


                        var user = res.getJSONObject("user")

                        SharedPreferences.setPrefernces(
                            this@Login, Constants.FILE_USER,
                            Constants.USER_EMAIL, user.getString("email")
                        )
                        SharedPreferences.setPrefernces(
                            this@Login, Constants.FILE_USER,
                            Constants.USER_NAME, user.getString("name")
                        )
                        SharedPreferences.setPrefernces(
                            this@Login, Constants.FILE_USER,
                            Constants.USER_TOKEN, res.getString("token")
                        )
                        SharedPreferences.setPrefernces(
                            this@Login, Constants.FILE_USER,
                            Constants.USER_ID, user.getString("_id")
                        )
                        Utils.sendRegistrationToServer(this)
                        runOnUiThread {
                            Toast.makeText(
                                this,
                                SharedPreferences.getID(this).toString(),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        val intent = Intent(this, MainActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)

//                       SharedPreferences.clearPreferences(this@MainActivity, Constants.FILE_USER)
                    } else {


                        runOnUiThread {
                            btn_login.isEnabled = true
                            Toast.makeText(
                                this@Login,
                                res.getString("errors"),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
                result.failure {

                    runOnUiThread {
                        btn_login.isEnabled = true
                        Toast.makeText(this@Login, it.localizedMessage, Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }


    }


    fun Signup() {
        val intent = Intent(this@Login, SignUp::class.java)

        intent.flags =
            Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

}

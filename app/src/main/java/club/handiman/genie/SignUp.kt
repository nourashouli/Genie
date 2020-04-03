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
import com.example.genie_cl.MainActivity
import com.github.kittinunf.result.failure
import com.github.kittinunf.result.success
import kotlinx.android.synthetic.main.fragment_registration.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.FirebaseException
import android.util.Log

import java.util.concurrent.TimeUnit

class SignUp : AppCompatActivity() {
    var code: String = ""
    var callback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(p0: PhoneAuthCredential) {
//code=p0.smsCode.toString()
            edt_verify_code.setText(p0.smsCode)
            //btn_verify.performClick()
        }

        override fun onVerificationFailed(p0: FirebaseException) {
            Log.d("OnCode", p0.localizedMessage)

        }

        override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
            Log.d("OnCode Sent", p0)
            ll_signup_form.visibility = View.GONE
            ll_verify_code.visibility = View.VISIBLE
            authToken = p0
        }

        override fun onCodeAutoRetrievalTimeOut(p0: String) {
            Log.d("OnCode", "Time out")
            super.onCodeAutoRetrievalTimeOut(p0)

        }


    }
    var fauth = PhoneAuthProvider.getInstance()
    var authToken: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.fragment_registration)
        super.onCreate(savedInstanceState)
        sign_in_button.setOnClickListener {
            openloginPage()
        }
        btn_verify.setOnClickListener {
            var credential =
                PhoneAuthProvider.getCredential(authToken!!, edt_verify_code.text.toString())

            FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {
                if (it.isSuccessful) {
                    register()
                } else {
                    runOnUiThread {
                        ll_signup_form.visibility = View.VISIBLE
                        ll_verify_code.visibility = View.GONE
                    }

                }
            }
        }

        fbtn_register.setOnClickListener {
            //make registration

            if (edt_phone.text.toString().isEmpty() ||
                name.text.toString().isEmpty() ||
                email_reg.text.toString().isEmpty() ||
                reg_password.text.toString().isEmpty() ||
                confirm.text.toString().isEmpty()
            ) {
                return@setOnClickListener
            } else {
                fauth.verifyPhoneNumber(
                    edt_phone.text.toString(), // Phone number to verify
                    60, // Timeout duration
                    TimeUnit.SECONDS, // Unit of timeout
                    this@SignUp, // Activity (for callback binding)
                    callback
                ) // OnVerificationStateChangedCallbacks
            }


        }

    }


    private fun register() {
        val name = name.text.toString()
        val email = email_reg.text.toString()
        val phone = edt_phone.text.toString()
        val password = reg_password.text.toString()
        val passwordConfirmation = confirm.text.toString()


        Fuel.post(
            Utils.API_Register,
            listOf(
                "password_confirmation" to passwordConfirmation,
                "name" to name, "email" to email,
                "password" to password, "phone" to phone,
                "role" to "user"

            )
        )
            .header("accept" to "application/json")
            .responseJson { _, _, result ->
                result.success {

                    var res = it.obj()
                    if (res.optString("status", "error") == "success") {

                        Utils.sendRegistrationToServer(this)
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


                        SharedPreferences.clearPreferences(this, Constants.FILE_USER)
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

    fun openloginPage() {
        val intent = Intent(this@SignUp, Login::class.java)

        intent.flags =
            Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}
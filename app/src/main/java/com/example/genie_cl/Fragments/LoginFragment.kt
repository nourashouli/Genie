package com.example.genie_cl.Fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.genie_cl.AuthenticationActivity
import com.example.genie_cl.MainActivity
import android.widget.Toast;
import com.example.genie_cl.R
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        registration_id.setOnClickListener{
            (context as AuthenticationActivity).navigateToFragment(RegistrationFragment())
        }

        fab_login_id.setOnClickListener {
            val toast = Toast.makeText(context as AuthenticationActivity, "Login Successfull", Toast.LENGTH_LONG)
            val toast1 = Toast.makeText(context as AuthenticationActivity, " wrong Email or Password", Toast.LENGTH_LONG)
            var email = Email!!.text.toString().trim()
            var password = Password!!.text.toString().trim()
            if (email=="Nourashouli@gmail.com"&&password=="Noura"){

            toast.show()

            startActivity(Intent(activity,MainActivity::class.java))}
             if (email!="Nourashouli@gmail.com"||password!="Noura")
            toast1.show()
        }

    }

}

package com.example.genie_cl.Fragments


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.genie_cl.AuthenticationActivity
import com.example.genie_cl.MainActivity

import com.example.genie_cl.R
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_registration.*


class RegistrationFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registration, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        sign_in_button.setOnClickListener{
            (context as AuthenticationActivity).navigateToFragment(LoginFragment())
        }

        fab_reg_id.setOnClickListener {
            val toast = Toast.makeText(context as AuthenticationActivity, "Sign up Successfull", Toast.LENGTH_LONG)

                toast.show()

                startActivity(Intent(activity,MainActivity::class.java))}

        }

    }



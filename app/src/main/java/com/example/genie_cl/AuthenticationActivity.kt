package com.example.genie_cl

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.genie_cl.Fragments.LoginFragment

class AuthenticationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)

        navigateToFragment(LoginFragment())

    }

    fun navigateToFragment(fragment: Fragment){
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.auth_main_frame,fragment)
            .commit()
    }
}

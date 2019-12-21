package com.example.genie_cl
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.genie_cl.Fragments.HomeFragment
import kotlinx.android.synthetic.main.activity_account_settings.*


class AccountSettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_settings)
            close_profile_btn.setOnClickListener {
                finish()
            } // setOnClickListener


        }
    }


package com.example.genie_cl.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.genie_cl.MainActivity
import android.content.Intent
import android.widget.Toast
import club.handiman.genie.TestingActivity
import com.example.genie_cl.R
import com.example.genie_cl.Utils.SharedPreferences
import com.example.genie_cl.Utils.Utils
import com.example.genie_cl.adapter.HomeAdapter
import com.example.genie_cl.requestForm
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.failure
import com.github.kittinunf.result.success
import kotlinx.android.synthetic.main.fragment_home.*
import org.jetbrains.anko.support.v4.runOnUiThread


class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    } // onCreateView

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
     requestbt.setOnClickListener {save()}
        initRecyclerView()
//        requestbt.setOnClickListener {
//            val intent = Intent(context!!, TestingActivity::class.java)
//            intent.flags =
//                Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
//            startActivity(intent)



    } // onActivityCreated

    private fun initRecyclerView(){

        home_recycler_view.layoutManager = LinearLayoutManager(context as MainActivity,LinearLayoutManager.VERTICAL,false)
        home_recycler_view.adapter = HomeAdapter(context as MainActivity,"home")

    }
    fun save() {

var f :String="5e7d3968e8deab6cd0066972"
        var r :String="5e7d3968e8deab6cd0066972"
       requestbt.isEnabled = false
        Fuel.post(
            Utils.API_MAKE_REQUEST, listOf(
                "employee_id" to f, "service_id" to r
            ,"description" to "descriptionnn of the request"

            )
        ).header(
            "accept" to "application/json",
            Utils.AUTHORIZATION to SharedPreferences.getToken(context!!).toString()
        )
            .responseJson {  _, _, result ->
                result.success {
                    val intent=Intent(context!!,requestForm::class.java)
                    startActivity(intent)
                }
                result.failure {
                    runOnUiThread {
                        Toast.makeText(context!!, it.localizedMessage.toString(), Toast.LENGTH_SHORT)
                    }
                }
            }

    }

} // HomeFragment

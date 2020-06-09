package club.handiman.genie.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.Toast
import club.handiman.genie.*
import com.example.genie_cl.R
import club.handiman.genie.Utils.SharedPreferences
import club.handiman.genie.Utils.Utils
import club.handiman.genie.Utils.putExtraJson
import club.handiman.genie.adapter.HomeAdapter
import com.example.genie_cl.adapter.utils.AdapterListener
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.failure
import com.github.kittinunf.result.success
import kotlinx.android.synthetic.main.fragment_home.*
import org.jetbrains.anko.support.v4.runOnUiThread
import org.json.JSONObject

class HomeFragment : Fragment() {
    var adapter: HomeAdapter? = null

    val tags = arrayListOf<String>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        home_recycler_view.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = HomeAdapter(requireContext())
        getRequest()
        getTgas()
        home_recycler_view.adapter = adapter
        smart_request.setOnClickListener {
            val intent = Intent(requireContext(), requestForm::class.java)
            intent!!.putExtraJson("object", "123")

             startActivity(intent)


        }

        val adapter_tags = ArrayAdapter(requireContext(), R.layout.item_list, tags)
        (search_input)?.setAdapter(adapter_tags)

        search_input!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                adapter!!.filter("title", charSequence.toString())
            }

            //stripe
            override fun afterTextChanged(editable: Editable) {}
        })



    }

    fun getRequest() {
        Fuel.get(Utils.API_POST)
            .header(
                "accept" to "application/json",
                Utils.AUTHORIZATION to SharedPreferences.getToken(requireContext()).toString()
            )
            .responseJson { _, _, result ->

                result.success {
                    var res = it.obj()
                    if (res.optString("status", "error") == "success") {

                        requireActivity().runOnUiThread {
                            val items = res.getJSONArray("posts")
                            for (i in 0 until items.length()) {
                                adapter!!.setItem(items.getJSONObject(i))
                            }
                        }

                    } else {
                        Toast.makeText(
                            activity,
                            res.getString("status"),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                result.failure {

                    Toast.makeText(activity, it.localizedMessage, Toast.LENGTH_LONG)
                        .show()
                }
            }
        search_input.visibility = View.VISIBLE

        Utils.hideSoftKeyBoard(requireContext(), search_input)
    }


    fun getTgas() {
        Fuel.get(Utils.API_Services)
            .header(
                "accept" to "application/json"
            )
            .responseJson { _, _, result ->

                result.success {
                    //
                    var res = it.obj()

                    if (res.optString("status", "error") == "success") {

                        //     var services = res.getJSONObject("services")
                        requireActivity().runOnUiThread {


                            val items = res.getJSONArray("services")

                            for (i in 0 until items.length()) {
                                tags.add(items.optJSONObject(i).optString("name"))
                                //  adapter.setItem(items.getJSONObject(i))
                            }


                        }
                    } else {

                        Toast.makeText(
                            activity,
                            res.getString("status"),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                result.failure {

                    Toast.makeText(activity, it.localizedMessage, Toast.LENGTH_LONG)
                        .show()
                }
            }
    }
}
// HomeFragment
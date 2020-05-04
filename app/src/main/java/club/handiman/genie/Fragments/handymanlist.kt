package club.handiman.genie.Fragments

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.Button
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import club.handiman.genie.Utils.Utils
import club.handiman.genie.adapter.HandymanListAdapter
import com.example.genie_cl.R
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.failure
import com.github.kittinunf.result.success
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_handymanlist.*
import kotlinx.android.synthetic.main.search_location.*
import org.json.JSONArray
import org.json.JSONObject

/**
 * A simple [Fragment] subclass.
 */
class handymanlist(var data: Any) : Fragment() {
    var adapter: HandymanListAdapter? = null

    var id: String? = null


    var latitude: Double? = 0.0
    var longitude: Double? = 0.0
    var arr: JSONArray? = null
    var HandymanLat: Double? = 0.0
    var HandymanLon: Double? = 0.0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(
            com.example.genie_cl.R.layout.fragment_handymanlist,
            container,
            false
        )

    } // onCreateView


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        id = (data as JSONObject).getString("_id")
        val adapter = HandymanListAdapter(context!!, id!!)

        handymanlist_recycler.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val id: String = (data as JSONObject).getString("_id")
        handymanlist_recycler.setAdapter(adapter)


        sort_price.setOnClickListener {
            adapter?.sort("price", "asc")

        }
        activity!!.runOnUiThread {
            sort_location.setOnClickListener {
                val dialog = getActivity()?.let { it1 -> Dialog(it1) }
                dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog?.window!!.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
                dialog?.setContentView(R.layout.search_location)
                val mapView =
                    dialog.findViewById<View>(R.id.dialogue_mapp) as MapView
                MapsInitializer.initialize(getActivity())
                mapView.onCreate(dialog.onSaveInstanceState())
                mapView.onResume()
                mapView.getMapAsync { googleMap ->
                    val rmayle = LatLng(33.61112, 35.4007)
                    googleMap.addMarker(MarkerOptions().position(rmayle).title("rami"))
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(rmayle))
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(rmayle, 13f))
                    googleMap.setOnMapClickListener { latLng ->
                        val marker = MarkerOptions()
                        marker.position(latLng)
                        latitude = latLng.latitude
                        longitude = latLng.longitude
                        marker.title(latLng.latitude.toString() + ":" + latLng.longitude)
                        googleMap.clear()
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13f))
                        googleMap.addMarker(marker)
                    }
                }
                val b =
                    dialog.findViewById<View>(R.id.dialogue_submit) as Button
                b.setOnClickListener {
                    dialog.dismiss()
                    latitude?.let { it1 ->
                        longitude?.let { it2 ->
                            adapter?.locationsort(
                                it1,
                                it2, "location", "location", "asc"
                            )
                        }
                    }


                }
                dialog.show()


            }
        }


        sss.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                adapter?.filter("name", s.toString())
            }

        })

        Fuel.get(Utils.API_HANDYMAN_BY_SERVICE.plus(id))
            .header(
                "accept" to "application/json"
            )
            .responseJson { _, _, result ->

                result.success {
                    //
                    var res = it.obj()


                    if (res.optString("status", "error") == "success") {


                        activity!!.runOnUiThread {
                            //                                Toast.makeText(
//                                    activity,
//                                    res.getJSONArray("handymen").toString(),
//                                    Toast.LENGTH_LONG
//                                ).show()
                            val items = res.getJSONArray("handymen")

                            for (i in 0 until items.length()) {
                                adapter?.setItem(items.getJSONObject(i))
                                arr = items.getJSONObject(i).getJSONArray("location")

                                HandymanLat = arr?.get(0) as Double
                                HandymanLon = arr?.get(1) as Double


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

    fun sortByLocation() {
        activity!!.runOnUiThread {
            val dialog = getActivity()?.let { it1 -> Dialog(it1) }
            dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog?.window!!.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            dialog?.setContentView(R.layout.search_location)
            val mapView =
                dialog.findViewById<View>(R.id.dialogue_mapp) as MapView
            MapsInitializer.initialize(getActivity())
            mapView.onCreate(dialog.onSaveInstanceState())
            mapView.onResume()
            mapView.getMapAsync { googleMap ->
                val rmayle = LatLng(33.61112, 35.4007)
                googleMap.addMarker(MarkerOptions().position(rmayle).title("rami"))
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(rmayle))
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(rmayle, 13f))
                googleMap.setOnMapClickListener { latLng ->
                    val marker = MarkerOptions()
                    marker.position(latLng)
                    latitude = latLng.latitude
                    longitude = latLng.longitude
                    marker.title(latLng.latitude.toString() + ":" + latLng.longitude)
                    googleMap.clear()
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13f))
                    googleMap.addMarker(marker)
                }
            }
            val b =
                dialog.findViewById<View>(R.id.dialogue_submit) as Button
            b.setOnClickListener {
                dialog.dismiss()
                latitude?.let { it1 ->
                    longitude?.let { it2 ->
                        adapter?.locationsort(
                            it1,
                            it2, "location", "location", "asc"
                        )
                    }
                }


            }
            dialog.show()

        }}
}
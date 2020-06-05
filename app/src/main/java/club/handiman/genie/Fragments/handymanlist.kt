package club.handiman.genie.Fragments

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import club.handiman.genie.Utils.Utils
import club.handiman.genie.Utils.putExtraJson
import club.handiman.genie.adapter.HandymanListAdapter
import club.handiman.genie.requestForm
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

class handymanlist(var data: Any) : Fragment() {
    var adapter: HandymanListAdapter? = null

    var id: String? = null

    var SelectedDay:String? = null
    var From:Int? = null
    var To:Int? = null

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
//        open_urgent_request.setOnClickListener {
//            val intent = Intent(context!!, requestForm::class.java)
//            var ob=JSONObject()
//            ob.put("id",id!!)
//            intent.putExtraJson("object", ob!!)
//            startActivity(intent)
//        }
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

        activity!!.runOnUiThread()
        {
            sort_time.setOnClickListener {
                val dialog = getActivity()?.let { it1 -> Dialog(it1) }
                dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog?.window!!.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
                dialog?.setContentView(R.layout.timer)
                val Fromtext:TextView = dialog.findViewById<View>(R.id.From_Time) as TextView
                val Totext:TextView = dialog.findViewById<View>(R.id.To_Time) as TextView
                val FromSeekbar:SeekBar = dialog.findViewById<View>(R.id.From_Seekbar) as SeekBar
                val ToSeekbar:SeekBar = dialog.findViewById<View>(R.id.To_Seekbar) as SeekBar
                val SubmitButton:Button = dialog.findViewById<View>(R.id.Submit_Timer)as Button
                val CancelButton:Button = dialog.findViewById<View>(R.id.Cancel_Timer)as Button
                val spin:Spinner = dialog.findViewById<View>(R.id.Time_Spinner) as Spinner
                val array = arrayOf("Choose Day","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday")
                val arrayAdapter1 =ArrayAdapter(context!!,android.R.layout.simple_spinner_item,array)
                spin.adapter = arrayAdapter1

                spin.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        SelectedDay = array[position]
                    }

                }




                // FromSeekbar.setOnSeekBarChangeListener()
                FromSeekbar.max = 24
                FromSeekbar.min = 0
                ToSeekbar.max = 24
                ToSeekbar.min = 0

                FromSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
                    override fun onProgressChanged(
                        seekBar: SeekBar?,
                        progress: Int,
                        fromUser: Boolean
                    ) {
                        Fromtext.text = progress.toString()
                        From = progress
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar?) {

                    }

                    override fun onStopTrackingTouch(seekBar: SeekBar?) {

                    }
                })
                ToSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
                    override fun onProgressChanged(
                        seekBar: SeekBar?,
                        progress: Int,
                        fromUser: Boolean
                    ) {
                        Totext.text = progress.toString()
                        To = progress
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar?) {

                    }

                    override fun onStopTrackingTouch(seekBar: SeekBar?) {

                    }

                })
                SubmitButton.setOnClickListener {
                    dialog.dismiss()
                    From?.let { it1 -> To?.let { it2 ->
                        adapter?.DateChooser(SelectedDay.toString(), it1,
                            it2
                        )
                    } }
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

        }
        )

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

        }
    }
}
package com.example.genie_cl
import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_request_form.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import android.view.View
import java.util.*
import com.example.genie_cl.Utils.Constants
import com.example.genie_cl.Utils.SharedPreferences
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.failure
import com.github.kittinunf.result.success
import com.example.genie_cl.Utils.Utils
import com.github.kittinunf.fuel.Fuel
import android.app.AlertDialog
import android.content.Intent
import android.location.LocationManager
import android.provider.Settings
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.places.Places
import com.google.android.gms.location.places.ui.PlacePicker
class requestForm2 : AppCompatActivity() {
    var formate = SimpleDateFormat("dd MMM, YYYY", Locale.US)
    private val PLACE_PICKER_REQUEST = 1
    private var mGoogleApiClient: GoogleApiClient? = null
    var date = null
    var obje = JSONObject(intent!!.extras!!.getString("object"))
    val id: String = (obje as JSONObject).getString("_id")
    val employee_id: String = (obje as JSONObject).getString("employee_id")
    val service_id: String = (obje as JSONObject).getString("service_id")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_request_form)

        var obje = JSONObject(intent!!.extras!!.getString("object"))
        //   Toast.makeText(this,obje.toString(),Toast.LENGTH_LONG).show()
      //  buildGoogleApiClient()

        //OnClickListener
        btPlacePicker.setOnClickListener(View.OnClickListener {
            if (!checkGPSEnabled()) {
                return@OnClickListener
            }
            val builder = PlacePicker.IntentBuilder()
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST)


        })
        btn_date.setOnClickListener {
            val now = Calendar.getInstance()
            val datePicker = DatePickerDialog(
                this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(Calendar.YEAR, year)
                    selectedDate.set(Calendar.MONTH, month)
                    selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    date = formate.format(selectedDate.time) as Nothing?
                    Toast.makeText(this, "date : " + date, Toast.LENGTH_SHORT).show()
                },
                now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()



        }

//        save_infor_profile_btn.setOnClickListener {
//            val des = description.text.toString()
//
//            save_infor_profile_btn.isEnabled = false
//            Fuel.post(
//                    Utils.API_MAKE_REQUEST, listOf(
//                        "description" to des,
//                        "date" to date, "employee_id" to employee_id,
//                        "service_id" to service_id, "user_id" to id
//                    )
//                )
//                .header("accept" to "application/json")
//                .responseJson { _, _, result ->
//                    result.success {
//
//                        var res = it.obj()
//                        if (res.optString("status", "error") == "success") {
//
//
//                            var request = res.getJSONObject("request")
//
//                          val intent = Intent(this, MainActivity::class.java)
//                            intent.flags =
//                                Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
//                            startActivity(intent)
//
//
////                       SharedPreferences.clearPreferences(this@MainActivity, Constants.FILE_USER)
//                        } else {
//                            Toast.makeText(
//                                this@requestForm2,
//                                res.getString("message"),
//                                Toast.LENGTH_LONG
//                            ).show()
//                        }
//                    }
//                    result.failure {
//                        runOnUiThread {
//                            Toast.makeText(
//                                    this@requestForm2,
//                                    it.localizedMessage,
//                                    Toast.LENGTH_LONG
//                                )
//                                .show()
//                        }
//                    }
//                }
//        }

    }


    private fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(this)
            .addApi(Places.GEO_DATA_API)
            .addApi(Places.PLACE_DETECTION_API)
            .enableAutoManage(this, null)
            .build()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == PLACE_PICKER_REQUEST && resultCode == Activity.RESULT_OK) {
            val place = PlacePicker.getPlace(data, this)
            val toastMsg = String.format("Place: %s", place.name)
            tvPlace.text = place!!.name.toString()
                .plus("\n".plus(place!!.address).plus("\n".plus(place!!.phoneNumber)))
            Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show()
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun isLocationEnabled(): Boolean {
        var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager!!.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkGPSEnabled(): Boolean {
        if (!isLocationEnabled())
            showAlert()
        return isLocationEnabled()
    }

    private fun showAlert() {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Enable Location")
            .setMessage("Locations Settings is set to 'Off'.\nEnable Location to use this app")
            .setPositiveButton("Location Settings") { paramDialogInterface, paramInt ->
                val myIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(myIntent)
            }
            .setNegativeButton("Cancel") { paramDialogInterface, paramInt -> }
        dialog.show()
    }
}
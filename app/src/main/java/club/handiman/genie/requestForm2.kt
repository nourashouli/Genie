package com.example.genie_cl
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import org.json.JSONObject
import com.example.genie_cl.Utils.Constants
import com.example.genie_cl.Utils.SharedPreferences
import com.example.genie_cl.Utils.Utils
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.failure
import com.github.kittinunf.result.success
import android.content.Intent
import android.content.IntentFilter
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_request_form.*
import java.text.SimpleDateFormat
import java.util.*

class requestForm2 : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_request_form)
        var Date:Date
        var date:String="g"
        var selectedTime:Calendar=Calendar.getInstance();
        var formate = SimpleDateFormat("dd MMM, YYYY", Locale.US)
        var timeFormat = SimpleDateFormat("hh:mm a", Locale.US)

            var obje:JSONObject = JSONObject(intent!!.extras!!.getString("object"))
            var object2 =JSONObject(obje.getString("nameValuePairs"))
            val employee_id: String = (object2 as JSONObject).optString("employee_id")
            val service_id: String = (object2 as JSONObject).optString("service_id")

        //  buildGoogleApiClient()

        btn_date.setOnClickListener {
            val now = Calendar.getInstance()
            val datePicker = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(Calendar.YEAR,year)
                selectedDate.set(Calendar.MONTH,month)
                selectedDate.set(Calendar.DAY_OF_MONTH,dayOfMonth)
                 date = formate.format(selectedDate.time)
                Toast.makeText(this,"date : " + date,Toast.LENGTH_SHORT).show()
            },
                now.get(Calendar.YEAR),now.get(Calendar.MONTH),now.get(Calendar.DAY_OF_MONTH))
            datePicker.show()
            try {
                if(btn_date.text != "Show Dialog") {
                    Date = timeFormat.parse(btn_date.text.toString())
                    now.time = Date
                }
            }catch (e:Exception){
                e.printStackTrace()
            }
            val timePicker = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                selectedTime = Calendar.getInstance()
                selectedTime.set(Calendar.HOUR_OF_DAY,hourOfDay)
                selectedTime.set(Calendar.MINUTE,minute)
                btn_date.text = timeFormat.format(selectedTime.time)
            },
                now.get(Calendar.HOUR_OF_DAY),now.get(Calendar.MINUTE),false)
            timePicker.show()

        }



        save_infor_profile_btn.setOnClickListener {
            val des = description.text.toString()

            save_infor_profile_btn.isEnabled = false
            Fuel.post(
                    Utils.API_MAKE_REQUEST, listOf(
                        "description" to des,
                        "date" to date, "employee_id" to employee_id,
                        "service_id" to service_id,"time" to selectedTime.toString()
                    )
                )
                .header("accept" to "application/json")
                .responseJson { _, _, result ->
                    result.success {

                        var res = it.obj()
                        if (res.optString("status", "error") == "success") {


                            var request = res.getJSONObject("request")

                          val intent = Intent(this, MainActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)

                        } else {
                            Toast.makeText(
                                this@requestForm2,
                                res.getString("message"),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                    result.failure {
                        runOnUiThread {
                            Toast.makeText(
                                    this@requestForm2,
                                    it.localizedMessage,
                                    Toast.LENGTH_LONG
                                )
                                .show()
                        }
                    }
                }
        }

    }


}
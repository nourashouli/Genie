package club.handiman.genie

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import club.handiman.genie.Utils.Constants
import club.handiman.genie.Utils.SharedPreferences
import club.handiman.genie.Utils.Utils
import com.example.genie_cl.R
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.failure
import com.github.kittinunf.result.success
import com.google.android.libraries.places.api.model.Place
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.rtchagas.pingplacepicker.PingPlacePicker
import com.wdullaer.materialdatetimepicker.time.Timepoint
import kotlinx.android.synthetic.main.fragment_request_form.*
import org.jetbrains.anko.toast
import org.json.JSONArray
import org.json.JSONObject
import java.sql.Time
import java.text.DateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class requestForm2 : AppCompatActivity(),
    com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener,
    com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener {
    var datePickerDialog: com.wdullaer.materialdatetimepicker.date.DatePickerDialog? = null
    var timePickerDialog: com.wdullaer.materialdatetimepicker.time.TimePickerDialog? = null
    var Year = 0
    var Month = 0
    var Day = 0
    var Hour = 0
    var Minute = 0
    var array: JSONArray? = null
    var day: JSONObject? = null
    var calendar: Calendar? = null
    var fileUri: Uri? = null
    var Dat = "j"
    var time: String = "h"
    var timee: String = "h"
    private val pingActivityRequestCode = 1001
    var employee_id: String = "H"
    var service_id: String = "H"
    var location = DoubleArray(2)
    var working_hours_and_hours = arrayOf<Array<Int>>()
    var day_of_week_currenctselection: Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_request_form)
        var Date: Date
        var obje: JSONObject = JSONObject(intent!!.extras!!.getString("object"))
        var object2 = JSONObject(obje.getString("nameValuePairs"))
        employee_id = (object2).optString("employee_id")
        service_id = (object2).optString("service_id")
        initTimeline()
        calendar = Calendar.getInstance()
        Year = calendar!!.get(Calendar.YEAR)
        Month = calendar!!.get(Calendar.MONTH)
        Day = calendar!!.get(Calendar.DAY_OF_MONTH)
        Hour = calendar!!.get(Calendar.HOUR_OF_DAY)
        Minute = calendar!!.get(Calendar.MINUTE)


        button_datepicker.setOnClickListener {

            datee()
            // timee()
        }
        btnOpenPlacePicker.setOnClickListener {
            showPlacePicker()
        }

        save_infor_profile_btn.setOnClickListener {

            save()
        }

        //listen to gallery button click
        gallery.setOnClickListener {
            pickPhotoFromGallery()
        }

        //listen to take photo button click
        takePhoto.setOnClickListener {
            askCameraPermission()
        }
        // initTimeline()

    }


    private fun showPlacePicker() {

        val builder = PingPlacePicker.IntentBuilder()
        builder.setAndroidApiKey(getString(R.string.android))
            .setMapsApiKey(getString(R.string.maps))

        try {
            val placeIntent = builder.build(this)
            startActivityForResult(placeIntent, pingActivityRequestCode)
        } catch (ex: Exception) {
            toast("Google Play Services is not Available")
        }
    }


    //pick a photo from gallery
    private fun pickPhotoFromGallery() {
        val pickImageIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(pickImageIntent, Constants.PICK_PHOTO_REQUEST)
    }

    //launch the camera to take photo via intent
    private fun launchCamera() {
        val values = ContentValues(1)
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
        fileUri = contentResolver
            .insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values
            )
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(packageManager) != null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
            intent.addFlags(
                Intent.FLAG_GRANT_READ_URI_PERMISSION
                        or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            )
            startActivityForResult(intent, Constants.TAKE_PHOTO_REQUEST)
        }
    }

    //ask for permission to take photo
    fun askCameraPermission() {
        Dexter.withActivity(this)
            .withPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {/* ... */
                    if (report.areAllPermissionsGranted()) {
                        //once permissions are granted, launch the camera
                        launchCamera()
                    } else {
                        Toast.makeText(
                            this@requestForm2,
                            "All permissions need to be granted to take photo",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {/* ... */
                    //show alert dialog with permission options
                    AlertDialog.Builder(this@requestForm2)
                        .setTitle(
                            "Permissions Error!"
                        )
                        .setMessage(
                            "Please allow permissions to take photo with camera"
                        )
                        .setNegativeButton(
                            android.R.string.cancel,
                            { dialog, _ ->
                                dialog.dismiss()
                                token?.cancelPermissionRequest()
                            })
                        .setPositiveButton(android.R.string.ok,
                            { dialog, _ ->
                                dialog.dismiss()
                                token?.continuePermissionRequest()
                            })
                        .setOnDismissListener({
                            token?.cancelPermissionRequest()
                        })
                        .show()
                }
            }).check()
    }

    override fun onActivityResult(
        requestCode: Int, resultCode: Int,
        data: Intent?
    ) {
        if ((requestCode == pingActivityRequestCode) && (resultCode == Activity.RESULT_OK)) {
            val place: Place? = PingPlacePicker.getPlace(data!!)
            toast("You selected: ${place!!.name}")

            location[0] = place!!.latLng!!.latitude!!.toDouble()
            location[1] = place!!.latLng!!.longitude!!.toDouble()
        }

        if (resultCode == Activity.RESULT_OK
            && requestCode == Constants.TAKE_PHOTO_REQUEST
        ) {
            //photo from camera
            //display the photo on the imageview
            imageView.setImageURI(fileUri)
        } else if (resultCode == Activity.RESULT_OK
            && requestCode == Constants.PICK_PHOTO_REQUEST
        ) {
            //photo from gallery
            fileUri = data?.data
            imageView.setImageURI(fileUri)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    fun datee() {
        datePickerDialog =
            com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
                this@requestForm2,
                Year,
                Month,
                Day
            )
        datePickerDialog!!.setThemeDark(false)
        datePickerDialog!!.showYearPickerFirst(false)
        datePickerDialog!!.setTitle("Date Picker")


        // Setting Min Date to today date
        val min_date_c = Calendar.getInstance()
        datePickerDialog!!.setMinDate(min_date_c)


        // Setting Max Date to next 2 years
        val max_date_c = Calendar.getInstance()
        max_date_c[Calendar.YEAR] = Year + 2
        datePickerDialog!!.setMaxDate(max_date_c)

        datePickerDialog!!.setOnCancelListener(DialogInterface.OnCancelListener {
            Toast.makeText(this@requestForm2, "canceled", Toast.LENGTH_SHORT)
                .show()


        })
        datePickerDialog!!.setOnDateSetListener { i, j, k, l ->

            var c: Calendar = Calendar.getInstance();

            c.set(l, k+1, j)

            Toast.makeText(this, "" + i + "\n" + j + "\n" + k + "\n" + l, Toast.LENGTH_LONG).show()


            var dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
             //thurs is two
            //friday is one
            // saturday is two
            // sunday is four
            //monday is five
            // tuesday six
            //wed is seven

          //  day_of_week_currenctselection = dayOfWeek - 2
            when (dayOfWeek) {
                0 -> {

                }
                1 -> {

                }
                2 -> {

                }
                3 -> {

                }
                4 -> {

                }
                5 -> {

                }
                6 -> {

                }
                7 -> {

                }
            }


          //  timee()
        }

        datePickerDialog!!.show(fragmentManager, "DatePickerDialog")
    }

    fun timee() {


        timePickerDialog =
            com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance(
                this@requestForm2,
                Hour,
                Minute,
                true
            )
        timePickerDialog!!.setThemeDark(false)
        timePickerDialog!!.enableMinutes(false)
        timePickerDialog!!.setTitle("Time Picker")
        //
        var arr: ArrayList<Int> = ArrayList<Int>()
        var counter = 0
        for (i in 0..23) {
            if (working_hours_and_hours[day_of_week_currenctselection!!][i] == 0) {
                arr.add(i)
                counter++
                // timepointArray!![counter++] = Timepoint(i)
            }
        }
        var timepointArray = Array<Timepoint>(counter) { i -> Timepoint(i) }
        counter--
        for (i in 0..counter) {
            timepointArray!![i] = Timepoint(arr.get(i))
        }

        timePickerDialog!!.setDisabledTimes(timepointArray)
        timePickerDialog!!.setOnCancelListener(DialogInterface.OnCancelListener {
            Toast.makeText(this@requestForm2, "Timepicker Canceled", Toast.LENGTH_SHORT)
                .show()
        })
        timePickerDialog!!.show(fragmentManager, "TimePickerDialog")
    }


    override fun onDateSet(

        view: com.wdullaer.materialdatetimepicker.date.DatePickerDialog,
        Year: Int,
        Month: Int,
        Day: Int
    ) {
        Dat = "      " + Day + "/" + (Month + 1) + "/" + Year

    }

    override fun onTimeSet(
        view: com.wdullaer.materialdatetimepicker.time.TimePickerDialog,
        hourOfDay: Int,
        minute: Int,
        second: Int
    ) {
        time = "Time: " + hourOfDay + "h" + minute + "m" + second + Dat
        timee = "" + hourOfDay + "h" + minute + "m" + second
        Toast.makeText(this@requestForm2, time, Toast.LENGTH_LONG).show()
        val text_timepicker =
            findViewById<View>(R.id.button_datepicker) as TextView
        text_timepicker.text = time
    }

    private fun initTimeline() {
        Fuel.get(Utils.API_timeline.plus(employee_id))
            .header(
                "accept" to "application/json",
                Utils.AUTHORIZATION to SharedPreferences.getToken(this).toString()
            )
            .responseJson { _, _, result ->
                result.success {
                    var res = it.obj()
                    if (res.optString("status", "error") == "success") {
                        runOnUiThread {
                            //
                            var values = res.getJSONArray("timeline")
                            //Toast.makeText(this, values.toString(), Toast.LENGTH_LONG).show()

                            for (i in 0 until values.length()) {
                                val day_in_values = values.getJSONArray(i)
                                var hours = arrayOf<Int>()
                                for (j in 0 until day_in_values.length()) {
                                    if (day_in_values.optBoolean(j)) {
                                        hours += 0
                                    } else {
                                        hours += 1
                                    }

                                }
                                working_hours_and_hours += hours

//
//
//                            }
                            }
                        }
                    }
                }

                result.failure {
                    runOnUiThread {
                        Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()
                    }
                }
            }
    }

    fun save() {
        var des: String = description!!.text!!.toString()
        val role = "user"
        //  save_infor_profile_btn.isEnabled = false
        Toast.makeText(this, fileUri.toString(), Toast.LENGTH_SHORT).show()
        Fuel.post(
            Utils.API_MAKE_REQUEST, listOf(
                "employee_id" to employee_id,
                "service_id" to service_id
                ,
                "description" to des,
                "role" to role,
                "latitude" to location[0],
                "longitude" to location[1],
                "date" to Dat,
                "to" to timee,
                "from" to timee,
                "image" to fileUri.toString()

            )
        ).header(
            "accept" to "application/json",
            Utils.AUTHORIZATION to SharedPreferences.getToken(this).toString()
        )
            .responseJson { _, _, result ->
                result.success {

                }
                result.failure {
                    runOnUiThread {
                        Toast.makeText(this, it.localizedMessage.toString()!!, Toast.LENGTH_SHORT)
                    }
                }
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
    }
}
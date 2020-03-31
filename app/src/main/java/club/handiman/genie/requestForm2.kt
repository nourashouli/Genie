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
import com.example.genie_cl.R
import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.net.Uri
import android.provider.MediaStore
import android.app.AlertDialog
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_request_form.*
import java.text.SimpleDateFormat
import java.util.*

class requestForm2 : AppCompatActivity() {
    var fileUri: Uri? = null
    var date:String="g"
    var selectedTime:Calendar=Calendar.getInstance();
    var formate = SimpleDateFormat("dd MMM, YYYY", Locale.US)
    var timeFormat = SimpleDateFormat("hh:mm a", Locale.US)

    var obje:JSONObject = JSONObject(intent!!.extras!!.getString("object"))
    var object2 =JSONObject(obje.getString("nameValuePairs"))
    val employee_id: String = (object2 as JSONObject).optString("employee_id")
    val service_id: String = (object2 as JSONObject).optString("service_id")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_request_form)
        var Date:Date


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
        Fuel.get(Utils.API_timeline.plus(employee_id))
            .header(
                "accept" to "application/json"
            )
            .responseJson { _, _, result ->

                result.success {
                    //
                    var res = it.obj()


                    if (res.optString("status", "error") == "success") {

                        Toast.makeText(
                            this,
                            res.getJSONArray("timeline").toString(),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }}

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

                }
    //pick a photo from gallery
    private fun pickPhotoFromGallery() {
        val pickImageIntent = Intent(Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(pickImageIntent, Constants.PICK_PHOTO_REQUEST)
    }

    //launch the camera to take photo via intent
    private fun launchCamera() {
        val values = ContentValues(1)
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
        fileUri = contentResolver
            .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if(intent.resolveActivity(packageManager) != null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                    or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            startActivityForResult(intent, Constants.TAKE_PHOTO_REQUEST)
        }
    }

    //ask for permission to take photo
    fun askCameraPermission(){
        Dexter.withActivity(this)
            .withPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {/* ... */
                    if(report.areAllPermissionsGranted()){
                        //once permissions are granted, launch the camera
                        launchCamera()
                    }else{
                        Toast.makeText(this@requestForm2, "All permissions need to be granted to take photo", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(permissions: List<PermissionRequest>, token: PermissionToken) {/* ... */
                    //show alert dialog with permission options
                    AlertDialog.Builder(this@requestForm2)
                        .setTitle(
                            "Permissions Error!")
                        .setMessage(
                            "Please allow permissions to take photo with camera")
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
                            token?.cancelPermissionRequest() })
                        .show()
                }

            }).check()

    }
    //
    //override function that is called once the photo has been taken
    override fun onActivityResult(requestCode: Int, resultCode: Int,
                                  data: Intent?) {
        if (resultCode == Activity.RESULT_OK
            && requestCode == Constants.TAKE_PHOTO_REQUEST) {
            //photo from camera
            //display the photo on the imageview
            imageView.setImageURI(fileUri)
        }else if(resultCode == Activity.RESULT_OK
            && requestCode == Constants.PICK_PHOTO_REQUEST){
            //photo from gallery
            fileUri = data?.data
            imageView.setImageURI(fileUri)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }


fun save(){
    val des = description.text.toString()

    save_infor_profile_btn.isEnabled = false
    Fuel.post(
        Utils.API_MAKE_REQUEST, listOf(
            "description" to des,
            "date" to date, "employee_id" to employee_id,
            "service_id" to service_id, "time" to selectedTime.toString(),"photo" to fileUri.toString()        )
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

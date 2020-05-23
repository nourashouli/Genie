package club.handiman.genie

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import club.handiman.genie.Utils.Constants
import club.handiman.genie.Utils.SharedPreferences
import club.handiman.genie.Utils.Utils
import club.handiman.genie.adapter.RequestImagesAdapter
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
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class requestForm2 : AppCompatActivity(),
    com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener,
    com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener {
    var datePickerDialog: com.wdullaer.materialdatetimepicker.date.DatePickerDialog? = null
    var timePickerDialog: com.wdullaer.materialdatetimepicker.time.TimePickerDialog? = null
    var timePickerDialogTo: com.wdullaer.materialdatetimepicker.time.TimePickerDialog? = null
    var Year = 0
    lateinit var imagePath: String
    var counter = 0
    var listOfImages = ArrayList<String>()
    val adapter = RequestImagesAdapter(this)
    var imagesPathList: MutableList<String> = arrayListOf()
    var PICK_IMAGE_MULTIPLE = 1
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
    var stringDate1: String? = null
    var location = DoubleArray(2)
    var working_hours = arrayOf<Array<Int>>()
    var day_of_week_currenctselection: Int? = null
    var request_date: String? = null
    var time_fromAsString: String? = null
    var time_fromAsInt: Int? = null
    var ex: String? = null
    var time_to: String? = null

    data class request(var date: String, var from: String, var to: String)

    var request_array: ArrayList<request>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_request_form)
        initAdapter()
        var Date: Date
        var obje: JSONObject = JSONObject(intent!!.extras!!.getString("object"))
        var object2 = JSONObject(obje.getString("nameValuePairs"))
        employee_id = (object2).optString("employee_id")
        service_id = (object2).optString("service_id")
        initTimeline()
        getHandymanRequests(employee_id)
        request_array = ArrayList<request>()
        calendar = Calendar.getInstance()
        calendar!!.firstDayOfWeek = Calendar.MONDAY
        Year = calendar!!.get(Calendar.YEAR)
        Month = calendar!!.get(Calendar.MONTH)
        Day = calendar!!.get(Calendar.DAY_OF_MONTH)
        Hour = calendar!!.get(Calendar.HOUR_OF_DAY)
        Minute = calendar!!.get(Calendar.MINUTE)


        button_datepicker.setOnClickListener {

            selectDate()
            //  timee()
        }
        btnOpenPlacePicker.setOnClickListener {
            showPlacePicker()
        }

        submit_request.setOnClickListener {

            save()
        }
        select_request_images.setOnClickListener {
            if (Build.VERSION.SDK_INT < 19) {

                var intent = Intent()
                intent.type = "image/*"
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(
                    Intent.createChooser(intent, "Select Picture")
                    , PICK_IMAGE_MULTIPLE
                )
            } else {
                var intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                intent.type = "image/*"
                startActivityForResult(intent, PICK_IMAGE_MULTIPLE);
            }
        }
    }


    private fun showPlacePicker() {

        val builder = PingPlacePicker.IntentBuilder()
        builder.setAndroidApiKey("AIzaSyDnGiz0xHyB0ktZ5Kt5L4BzNCAdE-qbB6w")
            .setMapsApiKey("AIzaSyAvIuv88TNm5f5M76GC3Or4k5OBRjfcJK0")

        try {
            val placeIntent = builder.build(this)
            startActivityForResult(placeIntent, pingActivityRequestCode)
        } catch (ex: Exception) {
            toast("Google Play Services is not Available")
        }
    }


    //pick a photo from gallery


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
        if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == Activity.RESULT_OK
            && null != data
        ) {

            adapter.setItem(data.data!!)

            val bitmap =
                MediaStore.Images.Media.getBitmap(contentResolver, data.data)
            listOfImages.add(Utils.encodeToBase64(bitmap))
            if (data.getClipData() != null) {

                var count = data.clipData!!.itemCount
                for (i in 0..count - 1) {
                    var imageUri: Uri = data!!.clipData!!.getItemAt(i).uri
                    getPathFromURI(imageUri)
                }
            } else if (data.getData() != null) {
                var imagePath: String? = data!!.data!!.path
                Log.e("imagePath", imagePath);
            }


        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun getPathFromURI(uri: Uri) {
        var path: String? = uri.path // uri = any content Uri

        val databaseUri: Uri
        val selection: String?
        val selectionArgs: Array<String>?
        if (path!!.contains("/document/image:")) { // files selected from "Documents"
            databaseUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            selection = "_id=?"
            selectionArgs = arrayOf(DocumentsContract.getDocumentId(uri).split(":")[1])
        } else { // files selected from all other sources, especially on Samsung devices
            databaseUri = uri
            selection = null
            selectionArgs = null
        }
        try {
            val projection = arrayOf(
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.ORIENTATION,
                MediaStore.Images.Media.DATE_TAKEN
            ) // some example data you can query
            val cursor = contentResolver.query(
                databaseUri,
                projection, selection, selectionArgs, null
            )
            if (cursor!!.moveToFirst()) {
                val columnIndex = cursor.getColumnIndex(projection[0])
                imagePath = cursor.getString(columnIndex)
                // Log.e("path", imagePath);
                imagesPathList.add(imagePath)


            }
            cursor.close()
        } catch (e: Exception) {
            Log.e("exception", e.message, e)
        }
    }

    private fun selectDate() {
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


        var hint = IntArray(7)
        for (index in 0..6) {

            var counter = 0
            for (i in 0..23) {

                if (working_hours[index][i] == 0) {

                    counter++
                } else {
                    break
                }
            }
            if (counter == 24) {
                hint[index] = 0
            } else {
                hint[index] = 1
            }
        }
        var loopdate = min_date_c
        while (min_date_c.before(max_date_c)) {
            val dayOfWeek = loopdate[Calendar.DAY_OF_WEEK]

            if ((dayOfWeek == Calendar.MONDAY && hint[0] == 0)
                || (dayOfWeek == Calendar.TUESDAY && hint[1] == 0)
                || (dayOfWeek == Calendar.WEDNESDAY && hint[2] == 0)
                || (dayOfWeek == Calendar.THURSDAY && hint[3] == 0)
                || (dayOfWeek == Calendar.FRIDAY && hint[4] == 0)
                || (dayOfWeek == Calendar.SATURDAY && hint[5] == 0)
                || (dayOfWeek == Calendar.SUNDAY && hint[6] == 0)
            ) {
                val disabledDays =
                    arrayOfNulls<Calendar>(1)
                disabledDays[0] = loopdate
                datePickerDialog!!.setDisabledDays(disabledDays)
            }
            min_date_c.add(Calendar.DATE, 1)
            loopdate = min_date_c
        }

        datePickerDialog!!.setOnCancelListener(DialogInterface.OnCancelListener {
            Toast.makeText(this@requestForm2, "canceled", Toast.LENGTH_SHORT)
                .show()


        })
        datePickerDialog!!.setOnDateSetListener { i, j, k, l ->


            calendar!!.set(l, k, j)
            var dateofl: String? = (l.toString())
            var monthofk: String? = ((k + 1).toString())
            if (dateofl!!.length == 1) {
                dateofl = "0".plus((l.toString()))
            }
            if (monthofk!!.length == 1) {
                monthofk = "0".plus(((k + 1).toString()))
            }
            ex = j.toString().plus("-").plus(monthofk).plus("-").plus(dateofl)
            stringDate1 = LocalDate.parse(ex).toString()

            val sdf2 = SimpleDateFormat("EEEE")

            val stringDate2: String = sdf2.format(Date(j, k, l - 1))
            request_date = stringDate2

            when (stringDate2) {
                "Monday" -> {
                    day_of_week_currenctselection = 0
                }
                "Tuesday" -> {
                    day_of_week_currenctselection = 1
                }
                "Wednesday" -> {
                    day_of_week_currenctselection = 2
                }
                "Thursday" -> {
                    day_of_week_currenctselection = 3
                }
                "Friday" -> {

                    day_of_week_currenctselection = 4
                }
                "Saturday" -> {

                    day_of_week_currenctselection = 5
                }
                "Sunday" -> {

                    day_of_week_currenctselection = 6
                }

            }
            Toast.makeText(this, day_of_week_currenctselection.toString(), Toast.LENGTH_LONG).show()

            selectTimeFrom()
        }

        datePickerDialog!!.show(fragmentManager, "DatePickerDialog")
    }

    private fun selectTimeFrom() {


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

        var arr: ArrayList<Int> = ArrayList<Int>()

        var arr2: ArrayList<Int> = ArrayList<Int>()
        var count = 0
        for (i in 0 until request_array!!.size) {
            val sdf2 = SimpleDateFormat("yyyy-MM-dd")
            val cc = sdf2.parse(request_array!!.get(i).date)
            val exco = sdf2.parse(ex)


            if (cc == exco) {
                for (j in request_array!!.get(i).from.toInt() until request_array!!.get(i).to.toInt()) {
                    arr2.add(j)
                    count++
                }
            }


        }


        var counter = 0
        for (i in 0..23) {
            if (working_hours[day_of_week_currenctselection!!][i] == 0) {
                arr.add(i)
                counter++
                //timepointArray!![counter++] = Timepoint(i)
            }
        }
        var timepointArray = Array<Timepoint>(counter + count) { i -> Timepoint(i) }
        counter--
        for (i in 0..counter) {
            timepointArray!![i] = Timepoint(arr.get(i))
        }
        count--
        for (i in 0..count) {
            timepointArray!![i] = Timepoint(arr2.get(i))
        }

        timePickerDialog!!.setDisabledTimes(timepointArray)
        timePickerDialog!!.setOnCancelListener(DialogInterface.OnCancelListener {
            Toast.makeText(this@requestForm2, "Timepicker Canceled", Toast.LENGTH_SHORT)
                .show()
        })
        timePickerDialog!!.setOnTimeSetListener { view, hourOfDay, minute, second ->
            time_fromAsInt = hourOfDay
            time_fromAsString = hourOfDay.toString()
            selectTimeTo()

        }
        timePickerDialog!!.show(fragmentManager, "TimePickerDialog")
    }

    private fun selectTimeTo() {

        timePickerDialogTo =
            com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance(
                this@requestForm2,
                time_fromAsInt!!,
                Minute,
                true
            )
        timePickerDialogTo!!.setThemeDark(false)
        timePickerDialogTo!!.enableMinutes(false)
        timePickerDialogTo!!.setTitle("Time Picker To")
        //
        var arr: ArrayList<Int> = ArrayList<Int>()
        var counter = 0
        var index = 0
        for (i in time_fromAsInt!!..23) {
            if (working_hours[day_of_week_currenctselection!!][i] == 0) {
                index = i
                break
            }
        }
        for (i in 0..time_fromAsInt!!) {
            counter++
            arr.add(i)
        }

        for (i in index!!..23) {
            counter++
            arr.add(i)
        }
        var timepointArray = Array<Timepoint>(counter) { i -> Timepoint(i) }
        counter--
        for (i in 0..counter) {
            timepointArray!![i] = Timepoint(arr.get(i))
        }

        timePickerDialogTo!!.setDisabledTimes(timepointArray)
        timePickerDialogTo!!.setOnCancelListener(DialogInterface.OnCancelListener {
            Toast.makeText(this@requestForm2, "Timepicker Canceled", Toast.LENGTH_SHORT)
                .show()
        })
        timePickerDialogTo!!.setOnTimeSetListener { view, hourOfDay, minute, second ->
            //            time_fromAsInt = hourOfDay
//            time_fromAsString = hourOfDay.toString()
            time_to = hourOfDay.toString()


        }
        timePickerDialogTo!!.show(fragmentManager, "TimePickerDialog")
    }

    override fun onDateSet(

        view: com.wdullaer.materialdatetimepicker.date.DatePickerDialog,
        Year: Int,
        Month: Int,
        Day: Int
    ) {
        Dat = "" + Year + "-" + (Month + 1) + "-" + Day

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
                                        hours += 1
                                    } else {
                                        hours += 0
                                    }

                                }
                                working_hours += hours

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
        val subject: String = subject!!.text!!.toString()


        var params = HashMap<String, String>()
        params.put("employee_id", employee_id)
        params.put("service_id", service_id)
        params.put("description", des)
        params.put("timezone", "Asia/Beirut")
        params.put("subject", subject)
        params.put("latitude", location[0].toString())
        params.put("longitude", location[1].toString())
        params.put("date", stringDate1.toString())
        params.put("to", time_to.toString())
        params.put("from", time_fromAsString.toString())
        for (x in 0 until listOfImages.size) {
            params.put("images[$x]", listOfImages.get(x))
        }

        Toast.makeText(this, fileUri.toString(), Toast.LENGTH_SHORT).show()
        Fuel.post(
            Utils.API_MAKE_REQUEST,
                params.toList()


        ).header(
            "accept" to "application/json",
            Utils.AUTHORIZATION to SharedPreferences.getToken(this).toString()
        )
            .responseJson { _, _, result ->
                result.success {
                    runOnUiThread {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }
                }
                result.failure {
                    runOnUiThread {
                        Toast.makeText(
                            this,
                            it.localizedMessage.toString()!!,
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }

            }
    }


    private fun getHandymanRequests(id: String) {
        Fuel.get(Utils.API_HANDYMAN_REQUESTS.plus(id))
            .header(
                "accept" to "application/json",
                Utils.AUTHORIZATION to SharedPreferences.getToken(this).toString()
            )
            .responseJson { _, _, result ->

                result.success {

                    val res = it.obj()

                    if (res.optString("status", "error") == "success") {
                        var list = res.getJSONArray("requests")

                        for (i in 0 until list.length()) {
                            val request = list.getJSONObject(i)
                            runOnUiThread {
                                request_array!!.add(
                                    request(
                                        request.optString("date", ""),
                                        request.optString("from", ""),
                                        request.optString("to", "")
                                    )

                                )
                            }

                        }


                    } else {

                        Toast.makeText(
                            this,
                            res.getString("status"),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                result.failure {

                    Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG)
                        .show()
                }


            }

    }

    fun initAdapter() {

        val mLayoutManager = GridLayoutManager(this, 2)

        recycler_request_images.setLayoutManager(mLayoutManager)

        recycler_request_images.addItemDecoration(GridSpacingItemDecoration(2, dpToPx(10), true))

        recycler_request_images.setItemAnimator(DefaultItemAnimator())

        recycler_request_images.setAdapter(adapter)


        recycler_request_images.setLayoutManager(mLayoutManager)

        recycler_request_images.addItemDecoration(GridSpacingItemDecoration(2, dpToPx(10), true))

        recycler_request_images.setItemAnimator(DefaultItemAnimator())


    }

    inner class GridSpacingItemDecoration(
        private val spanCount: Int,
        private val spacing: Int,
        private val includeEdge: Boolean
    ) : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val position = parent.getChildAdapterPosition(view) // item position
            val column = position % spanCount // item column

            if (includeEdge) {
                outRect.left =
                    spacing - column * spacing / spanCount // spacing - column * ((1f / spanCount) * spacing)
                outRect.right =
                    (column) * spacing / spanCount // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing
                }
                outRect.bottom = spacing // item bottom
            } else {
                outRect.left = column * spacing / spanCount // column * ((1f / spanCount) * spacing)
                outRect.right =
                    spacing - (column) * spacing / spanCount // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private fun dpToPx(dp: Int): Int {
        val r = resources
        return Math.round(
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp.toFloat(),
                r.displayMetrics
            )
        )
    }
}

package club.handiman.genie

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import club.handiman.genie.Models.AddressModel
import club.handiman.genie.Utils.SharedPreferences
import club.handiman.genie.Utils.Utils
import club.handiman.genie.adapter.AddressAdapter
import club.handiman.genie.adapter.RequestImagesAdapter
import com.example.genie_cl.R
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.failure
import com.github.kittinunf.result.success
import com.google.android.libraries.places.api.model.Place
import com.rtchagas.pingplacepicker.PingPlacePicker
import kotlinx.android.synthetic.main.fragment_request_form.*
import org.jetbrains.anko.toast
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList

class requestForm : AppCompatActivity(),
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
    var address: Any? = null
    var isUrgent: Boolean = false
    private val pingActivityRequestCode = 1001

    var service_id: String? = null
    var stringDate1: String? = null
    var location = DoubleArray(2)
    var day_of_week_currenctselection: Int? = null
    var time_fromAsString: String? = null
    var time_fromAsInt: Int? = null
    var ex: String? = null
    var time_to: String? = null
    var addresses_: ArrayList<Any>? = ArrayList()
    var customDropDownAdapter: AddressAdapter? = null
    var address_id:Any?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_request_form)
        initAdapter()
        initAddresses()
        is_urgent.setOnClickListener {
            if (!isUrgent) {
                isUrgent = true
                date_text.visibility = View.GONE
                button_datepicker.visibility = View.GONE
            } else {
                isUrgent = false
                date_text.visibility = View.VISIBLE
                button_datepicker.visibility = View.VISIBLE
            }
        }


        var spinner: Spinner = codeSpinner


        customDropDownAdapter = AddressAdapter(this, addresses_!!)

        spinner.adapter = customDropDownAdapter



        spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                address_id=(addresses_!![position] as JSONObject).optString("_id")

                Toast.makeText(
                    this@requestForm,
                    addresses_!![position].toString()!!,
                    Toast.LENGTH_SHORT
                )
                    .show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }


        var Date: Date
        var obje = JSONObject(intent!!.extras!!.getString("object"))
        var object2 = JSONObject(obje.getString("nameValuePairs"))
        service_id = (object2).optString("id")


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
                this@requestForm,
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
            Toast.makeText(this@requestForm, "canceled", Toast.LENGTH_SHORT)
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
                this@requestForm,
                Hour,
                Minute,
                true
            )
        timePickerDialog!!.setThemeDark(false)
        timePickerDialog!!.enableMinutes(false)
        timePickerDialog!!.setTitle("Time Picker")



        timePickerDialog!!.setOnCancelListener(DialogInterface.OnCancelListener {
            Toast.makeText(this@requestForm, "Timepicker Canceled", Toast.LENGTH_SHORT)
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
                this@requestForm,
                time_fromAsInt!!,
                Minute,
                true
            )
        timePickerDialogTo!!.setThemeDark(false)
        timePickerDialogTo!!.enableMinutes(false)
        timePickerDialogTo!!.setTitle("Time Picker To")
        //


        timePickerDialogTo!!.setOnCancelListener(DialogInterface.OnCancelListener {
            Toast.makeText(this@requestForm, "Timepicker Canceled", Toast.LENGTH_SHORT)
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
        Toast.makeText(this@requestForm, time, Toast.LENGTH_LONG).show()
        val text_timepicker =
            findViewById<View>(R.id.button_datepicker) as TextView
        text_timepicker.text = time
    }


    fun save() {
        var des: String = description!!.text!!.toString()
        val role = "user"
        val subject: String = subject!!.text!!.toString()


        var params = HashMap<String, String>()
        if (!isUrgent) {
            params.put("is_urgent", "false")
            params.put("date", stringDate1.toString())
            params.put("to", time_to.toString())
            params.put("from", time_fromAsString.toString())

        } else {
            params.put("is_urgent", "true")


        }
        params.put("service_id", service_id.toString())
        params.put("description", des)
        params.put("timezone", "Asia/Beirut")
        params.put("subject", subject)
        params.put("address", address_id!!.toString())

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

    fun initAddresses() {
        Fuel.get(Utils.API_EDIT_PROFILE)
            .header(
                "accept" to "application/json",
                Utils.AUTHORIZATION to SharedPreferences.getToken(baseContext).toString()
            )
            .responseJson { _, _, result ->

                result.success {

                    var res = it.obj()

                    if (res.optString("status", "error") == "success") {

                        var profile = res.getJSONObject("profile")
                        runOnUiThread {
                            if (profile.has("client_addresses")) {
                                val items = profile.optJSONArray("client_addresses")
                                //   addresses_ = ArrayList<String>()
                                for (item in 0 until items!!.length()) {

                                    addresses_!!.add((items.get(item) as JSONObject))
                                    customDropDownAdapter!!.notifyDataSetChanged()
                                }
                                //      user_addresses = profile.optJSONArray("client_addresses")
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

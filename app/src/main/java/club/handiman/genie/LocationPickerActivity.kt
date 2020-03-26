package club.handiman.genie

//import android.app.Activity
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import com.example.genie_cl.R
//class LocationPickerActivity : AppCompatActivity() {
//    private val pingActivityRequestCode = 1001
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_location_picker)
//    }
//
//    private fun showPlacePicker() {
//
//        val builder = PingPlacePicker.IntentBuilder()
//
//        builder.setAndroidApiKey(getString(R.string.google_api_key))
//            .setMapsApiKey(getString(R.string.google_maps_key))
//
//        // If you want to set a initial location
//        // rather then the current device location.
//        // pingBuilder.setLatLng(LatLng(37.4219999, -122.0862462))
//
//        try {
//            val placeIntent = builder.build(this)
//            startActivityForResult(placeIntent, pingActivityRequestCode)
//        } catch (ex: Exception) {
//            Toast.makeText(this, "Google Play Services is not Available", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if ((requestCode == pingActivityRequestCode) && (resultCode == Activity.RESULT_OK)) {
//
//            val place: Place? = PingPlacePicker.getPlace(data!!)
//            Toast.makeText(
//                this, "You selected:${place!!.latLng!!.latitude} and" +
//                        "  ${place?.name}" +
//                        " " +
//                        "", Toast.LENGTH_SHORT
//            ).show()
//
//
//        }
//    }
//}

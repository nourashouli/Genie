package club.handiman.genie

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.genie_cl.R
import java.text.SimpleDateFormat
import java.util.*
class requestForm : AppCompatActivity() {
    var formate = SimpleDateFormat("dd MMM, YYYY",Locale.US)
    var timeFormat = SimpleDateFormat("hh:mm a", Locale.US)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_request_form)
//
//        btn_date.setOnClickListener {
//            val now = Calendar.getInstance()
//            val datePicker = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
//                val selectedDate = Calendar.getInstance()
//                selectedDate.set(Calendar.YEAR,year)
//                selectedDate.set(Calendar.MONTH,month)
//                selectedDate.set(Calendar.DAY_OF_MONTH,dayOfMonth)
//                val date = formate.format(selectedDate.time)
//                Toast.makeText(this,"date : " + date,Toast.LENGTH_SHORT).show()
//            },
//                    now.get(Calendar.YEAR),now.get(Calendar.MONTH),now.get(Calendar.DAY_OF_MONTH))
//            datePicker.show()
//            try {
//                if(btn_date.text != "Show Dialog") {
//                    val date = timeFormat.parse(btn_date.text.toString())
//                    now.time = date
//                }
//            }catch (e:Exception){
//                e.printStackTrace()
//            }
//            val timePicker = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
//                val selectedTime = Calendar.getInstance()
//                selectedTime.set(Calendar.HOUR_OF_DAY,hourOfDay)
//                selectedTime.set(Calendar.MINUTE,minute)
//                btn_date.text = timeFormat.format(selectedTime.time)
//            },
//                now.get(Calendar.HOUR_OF_DAY),now.get(Calendar.MINUTE),false)
//            timePicker.show()
//
//        }
    }
}
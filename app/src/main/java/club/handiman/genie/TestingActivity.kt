package club.handiman.genie


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.genie_cl.R
import com.example.genie_cl.Utils.Constants
import com.example.handymanapplication.adapters.NotificationAdapter
import kotlinx.android.synthetic.main.activity_chat_log.*
import org.json.JSONObject

class TestingActivity : AppCompatActivity() {
    val adapter = NotificationAdapter(this)

    private val aLBReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            try {
                var msg = JSONObject(intent!!.extras!!.getString(Constants.PARAM_NOTIFICATION_INFO))


                Toast.makeText(context,msg.optString("email") , Toast.LENGTH_LONG).show()

                adapter.setItem(msg)

                // the activity is notified that there is a new message in the intent

            } catch (e: Exception) {

            }

        }


    }

    override fun onPause() {
        unregisterReceiver(aLBReceiver)
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(aLBReceiver, IntentFilter().apply {
            addAction(Constants.NOTIFICATION_BROADCAST_RECEIVER_MESSAGE_EVENT)
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)
        registerReceiver(aLBReceiver, IntentFilter().apply {
            addAction(Constants.NOTIFICATION_BROADCAST_RECEIVER_MESSAGE_EVENT)
        })



        recyclerview_chat_log.layoutManager = LinearLayoutManager(this@TestingActivity,
            LinearLayoutManager.VERTICAL, true)
        recyclerview_chat_log.adapter = adapter
        var ob = JSONObject()
        ob!!.put("description", "des")
        adapter.setItem(ob)
        adapter.setItem(ob)





    }


}

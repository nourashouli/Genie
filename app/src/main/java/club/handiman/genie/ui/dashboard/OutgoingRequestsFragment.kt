package club.handiman.genie.ui.dashboard
import androidx.fragment.app.Fragment
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.QuickViewConstants
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RemoteViews
import com.example.genie_cl.R
import kotlinx.android.synthetic.main.fragment_noti.*

class OutgoingRequestsFragment : Fragment()  {

//    //declaring variables
//    lateinit var notificationManager : NotificationManager
//    lateinit var notificationChannel : NotificationChannel
//    lateinit var builder : Notification.Builder
//    private val channelId = "i.apps.notifications"
//    private val description = "Test notification"

     override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_noti, container, false)
    }

override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
        //accessing button
        //it is a class to notify the user of events that happen.
        // This is how you tell the user that something has happened in the
//    background.notificationManager =
//        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    //onClick listener for the button
       // btn.setOnClickListener {

            //pendingIntent is an intent for future use i.e after
            // the notification is clicked, this intent will come into action



            //FLAG_UPDATE_CURRENT specifies that if a previous
            // PendingIntent already exists, then the current one
            // will update it with the

            // 0 is the request code, using it later with the
            // same method again will get back the same pending
            // intent for future reference
            //intent passed here is to our afterNotification class


            //RemoteViews are used to use the content of
            // some different layout apart from the current activity layout


            //checking if android version is greater than oreo(API 26) or not
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                notificationChannel = NotificationChannel(
//                    channelId,description,NotificationManager.IMPORTANCE_HIGH)
//                notificationChannel.enableLights(true)
//                notificationChannel.lightColor = Color.GREEN
//                notificationChannel.enableVibration(false)
//                notificationManager.createNotificationChannel(notificationChannel)
//
//            }else{
//
//            }
//            notificationManager.notify(1234,builder.build())
//        }

}}
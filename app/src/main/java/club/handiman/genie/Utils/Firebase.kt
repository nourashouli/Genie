package club.handiman.genie.Utils




import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import club.handiman.genie.Fragments.ChatLog.ChatLogActivity
import club.handiman.genie.TestingActivity
import com.example.genie_cl.R
import java.util.*


import org.json.JSONObject
import java.text.SimpleDateFormat

class Firebase : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.data.isNotEmpty()) {
            var dataJson = JSONObject(remoteMessage.data["data"].toString())
            var type = dataJson.getString("type")
            when(type) {

                "comment" -> {
                   }
                "request" -> {

                }
                "message" -> {
                    sendBrodcastNotification(dataJson)
                    sendRegularNotification(dataJson, Intent(baseContext, ChatLogActivity::class.java))

                }
            }

            for(  key in remoteMessage.data.keys){
                Log.e(key, remoteMessage.data[key].toString())
            }
        }


    }


    override fun onNewToken(token: String) {

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        Utils.sendRegistrationToServer(baseContext)
    }


    private fun sendRegularNotification(notificationCommentInfo: JSONObject, intent: Intent) {

        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        intent.putExtra(Constants.PARAM_NOTIFICATION_INFO, notificationCommentInfo.toString())
        val resultIntentNotificationClicked =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        val title = "Grey"
        val message = notificationCommentInfo.optString("message")

        val notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val mNotificationBuilder = NotificationCompat.Builder(this)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setSound(notificationSoundURI)
            .setContentIntent(resultIntentNotificationClicked)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationID =
            Integer.parseInt(SimpleDateFormat("HHmmssSSS", Locale.UK).format(Date()))
        notificationManager.notify(notificationID, mNotificationBuilder.build())
    }

    private fun sendBrodcastNotification(notificationCommentInfo: JSONObject) {
        val intent = Intent()
        intent.action = Constants.NOTIFICATION_BROADCAST_RECEIVER_MESSAGE_EVENT
        intent.putExtra(Constants.PARAM_NOTIFICATION_INFO, notificationCommentInfo.toString())
        sendBroadcast(intent)
    }

}

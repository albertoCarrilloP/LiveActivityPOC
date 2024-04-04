package com.wizeline.liveactivitypoc

import android.app.ActivityManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wizeline.liveactivitypoc.ScreenStateReceiver.Companion.isFullScreenActivityDisplayed

class MyFullScreenActivity : ComponentActivity() {

    private lateinit var unlockReceiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED)
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        setContent {
            val viewModel: RecordRideNotificationViewModel = viewModel()

            val progressState = viewModel.progressState.collectAsState()
            CardDidi(progressState.value, onBackgroundClicked = {
                isFullScreenActivityDisplayed = false
                finish()
            })

        }
        registerBroadcast()
    }


    private fun registerBroadcast() {
        unlockReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (intent.action == Intent.ACTION_USER_PRESENT) {
                    if (isAppInForeground()) {
                        isFullScreenActivityDisplayed = false
                        finish()
                    }
                }
            }
        }

        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_USER_PRESENT)
        }
        registerReceiver(unlockReceiver, filter)
    }

    private fun isAppInForeground(): Boolean {
        val activityManager = this.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningAppProcesses = activityManager.runningAppProcesses ?: return false

        return runningAppProcesses.any {
            it.processName.equals(this.packageName, ignoreCase = true) &&
                    it.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
        }
    }
}

@Composable
fun CardDidi(status: Int, onBackgroundClicked: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .clickable { onBackgroundClicked() }
    ) {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth(.9f)
                .align(Alignment.BottomCenter)
                .padding(bottom = 48.dp)
                .background(Color.White, ShapeDefaults.ExtraLarge)
        ) {
            Column(
                Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Bottom
            ) {
                Row(Modifier.fillMaxWidth()) {
                    Image(
                        modifier = Modifier.size(20.dp),
                        painter = painterResource(id = R.drawable.ic_didi),
                        contentDescription = "Test"
                    )
                    Text(
                        text = "NOT Didi food",
                        modifier = Modifier.padding(start = 5.dp, top = 3.dp), fontSize = 13.sp
                    )
                }

                Spacer(modifier = Modifier.size(15.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.weight(1f)) {
                        Text(
                            text = buildAnnotatedString {
                                append("Entrega estimada: ")
                                withStyle(
                                    SpanStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                ) {
                                    append("3:18PM")
                                }
                            }, fontSize = 13.sp
                        )
                        Text(
                            text = "Preparando tu pedido",
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.width(50.dp))

                    Image(
                        modifier = Modifier.size(48.dp),
                        painter = painterResource(id = R.drawable.ic_delivery),
                        contentDescription = "Test"
                    )
                }

                Spacer(modifier = Modifier.size(15.dp))

                LinearProgressIndicator(progress = status * 0.25f)

                Spacer(modifier = Modifier.size(15.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FullScreenNotificationPrev() {
    CardDidi(3, onBackgroundClicked = {})
}

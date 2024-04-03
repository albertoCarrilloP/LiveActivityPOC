package com.wizeline.liveactivitypoc

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.lifecycleScope
import com.wizeline.liveactivitypoc.ui.theme.LiveActivityPOCTheme
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val pushNotificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LiveActivityPOCTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LiveActivityAttempts(
                        onWidgetClicked = { startWidgetRunningService() },
                        onPersistentNotificationClicked = { runPersistentNotifications() }
                    )
                }
            }
        }
        createNotificationChannel()
        addPostNotificationPermissions()
    }

    private fun runPersistentNotifications() {
        lifecycleScope.launch {
            var distance = 0
            var time = 0
            while (true) {
                createPersistentNotification(distance, time)
                delay(1000)
                distance += 37
                time += 1
                if (time == 30) cancel()
            }
        }
    }

    private fun addPostNotificationPermissions() {
        val areNotificationsEnabled =
            NotificationManagerCompat.from(this@MainActivity).areNotificationsEnabled()
        if (areNotificationsEnabled) return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            pushNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    private fun startWidgetRunningService() {
        val serviceIntent = Intent(this, WidgetRunningService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent)
        } else {
            startService(serviceIntent)
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Running info"
            val descriptionText = "description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("CHANNEL_ID", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createPersistentNotification(distance: Int, time: Int) {
        val notificationBuilder = NotificationCompat.Builder(this, "CHANNEL_ID")
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setSmallIcon(R.drawable.ic_run)
            .setContentTitle("Running Distance")
            .setContentText("$distance mts - 00:$time s")
            .setCategory(Notification.CATEGORY_SERVICE)
            .setPriority(NotificationCompat.PRIORITY_LOW)

        val notification = notificationBuilder.build()
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            NotificationManagerCompat.from(this).notify(1, notification)
        } else {
            NotificationManagerCompat.from(this).notify(1, notification)
        }
    }
}

@Composable
fun LiveActivityAttempts(
    onWidgetClicked: () -> Unit,
    onPersistentNotificationClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        Spacer(Modifier.weight(1f))
        Button80Percent("Attempt 1 Widget", onWidgetClicked)
        Button80Percent("Attempt 2 Persistent Notification", onPersistentNotificationClicked)
        Spacer(Modifier.weight(1f))
    }
}

@Composable
private fun Button80Percent(text: String, onWidgetClicked: () -> Unit) {
    Button(modifier = Modifier.fillMaxWidth(.8f), onClick = { onWidgetClicked() }) {
        Text(
            text = text,
            fontSize = 16.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LiveActivityPOCTheme {
        LiveActivityAttempts(onWidgetClicked = {}, onPersistentNotificationClicked = {})
    }
}
package com.wizeline.liveactivitypoc

import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel

class ExampleService : Service() {
    private val serviceScope = CoroutineScope(Dispatchers.Default)
    private val screenStateReceiver = ScreenStateReceiver()
    private var isForegroundService: Boolean = false

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_SCREEN_OFF)
        }
        registerReceiver(screenStateReceiver, filter)
        startTracking()
        return START_STICKY
    }

    private fun startTracking() {
        val notification = NotificationCompat.Builder(this, "FullScreenChannel")
            .setContentTitle("Ejemplo de Servicio")
            .setContentText("Este servicio está corriendo en segundo plano.")
            .setSmallIcon(R.drawable.ic_run)
            .build()

        if (!isForegroundService) {
            isForegroundService = true
            startForeground(NOTIFICATION_ID, notification)
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }
}
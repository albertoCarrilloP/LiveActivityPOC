package com.wizeline.liveactivitypoc

import android.app.Service
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

const val NOTIFICATION_ID = 1234

class WidgetRunningService : Service() {
    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private var distance = 0
    private var time = 0

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startTracking()
        coroutineScope.launch {
            while (true) {
                updateData()
                delay(1000)
            }
        }
        return START_STICKY
    }

    private fun updateData() {
        if (time == 30) {
            stopSelf()
            coroutineScope.cancel()
            return
        }
        val sharedPreferences = getSharedPreferences("WidgetData", Context.MODE_PRIVATE)
        distance += 37
        time += 1
        with(sharedPreferences.edit()) {
            putInt("RunningDistance", distance)
            putInt("RunningTime", time)
            apply()
        }
        val appWidgetManager = AppWidgetManager.getInstance(this)
        val thisWidget = ComponentName(this, RunningDistanceWidget::class.java)
        val allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget)

        allWidgetIds.forEach { appWidgetId ->
            RunningDistanceWidget.updateAppWidget(this, appWidgetManager, appWidgetId)
        }
    }

    private fun startTracking() {
        val notification = NotificationCompat.Builder(this, "CHANNEL_ID")
            .setContentTitle("Running service")
            .setContentText("service running un background")
            .setSmallIcon(R.drawable.ic_run)
            .build()
        startForeground(NOTIFICATION_ID, notification)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

}
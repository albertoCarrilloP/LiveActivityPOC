package com.wizeline.liveactivitypoc

import android.app.Service
import android.content.Intent
import android.os.IBinder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ExampleService : Service() {
    private val serviceScope = CoroutineScope(Dispatchers.Default)

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        serviceScope.launch {
            delay(5000)
            val fullScreenIntent = Intent(this@ExampleService.applicationContext, MyFullScreenActivity::class.java)
            fullScreenIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(fullScreenIntent)
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }
}
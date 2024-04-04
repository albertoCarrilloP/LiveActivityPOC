package com.wizeline.liveactivitypoc

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ScreenStateReceiver : BroadcastReceiver() {
    companion object {
        var isFullScreenActivityDisplayed = false
    }

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_SCREEN_OFF -> {
                if (isFullScreenActivityDisplayed) return
                val fullScreenIntent = Intent(context, MyFullScreenActivity::class.java)
                fullScreenIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(fullScreenIntent)
                isFullScreenActivityDisplayed = true
            }
        }
    }
}
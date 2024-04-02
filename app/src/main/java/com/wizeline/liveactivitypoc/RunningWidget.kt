package com.wizeline.liveactivitypoc

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews

class RunningDistanceWidget : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    companion object {
        internal fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            val sharedPreferences = context.getSharedPreferences("WidgetData", Context.MODE_PRIVATE)
            val distance = sharedPreferences.getInt("RunningDistance", 0)
            val time = sharedPreferences.getInt("RunningTime", 0)

            val views = RemoteViews(context.packageName, R.layout.widget_running)
            views.setTextViewText(
                R.id.widget_distance_text,
                context.getString(R.string.your_total_distance, distance)
            )
            views.setTextViewText(
                R.id.widget_time_text,
                context.getString(R.string.time_running, time.toString())
            )

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}

package com.lucifer.newsapplication.background

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

// It is a broadcast receiver, every time it is called background service will execute.
class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val background = Intent(context, BackgroundService::class.java)
        context.startService(background)
    }
}
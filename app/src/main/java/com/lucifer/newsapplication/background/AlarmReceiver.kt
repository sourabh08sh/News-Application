package com.lucifer.newsapplication.background

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent


class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val background = Intent(context, BackgroundService::class.java)
        context.startService(background)
    }
}
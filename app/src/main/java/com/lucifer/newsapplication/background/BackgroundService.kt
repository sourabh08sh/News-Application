package com.lucifer.newsapplication.background

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.lucifer.newsapplication.repository.MainRepository
import com.lucifer.newsapplication.utils.Coroutines
import dagger.android.AndroidInjection
import javax.inject.Inject

// it runs on a different thread whenever a broadcast receiver calls it.
class BackgroundService : Service() {
    @Inject
    lateinit var repository: MainRepository

    private var isRunning = false
    private var context: Context? = null
    private var backgroundThread: Thread? = null

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        AndroidInjection.inject(this)
        context = this
        isRunning = false
        this.backgroundThread = Thread(myTask)
    }

    private val myTask = Runnable {
        Log.d("service", "running")

        Coroutines.io{
            val notificationManager: NotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            repository.getNewsBackground(notificationManager) // it is called after every 5 min.
        }
        stopSelf()
    }

    override fun onDestroy() {
        isRunning = false
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (!isRunning) {
            isRunning = true
            this.backgroundThread!!.start()
        }
        return START_STICKY
    }
}
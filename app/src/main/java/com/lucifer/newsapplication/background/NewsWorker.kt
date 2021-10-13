package com.lucifer.newsapplication.background

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.lucifer.newsapplication.MainApplication
import com.lucifer.newsapplication.utils.Coroutines

class NewsWorker(private val context: Context, params: WorkerParameters) : Worker(context, params){
    override fun doWork(): Result {
        Log.d("WorkManager", "running")
        val repository = (context as MainApplication).repository
        Coroutines.io{
            repository.getNewsBackground()
        }
        return Result.success()
    }
}
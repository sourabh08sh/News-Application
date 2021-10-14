package com.lucifer.newsapplication.background

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.lucifer.newsapplication.repository.MainRepository
import com.lucifer.newsapplication.utils.Coroutines

class NewsWorker (private val context: Context, params: WorkerParameters, private val repository: MainRepository) : Worker(context, params){
    override fun doWork(): Result {
        Log.d("WorkManager", "running")
        Coroutines.io{
            repository.getNewsBackground()
        }
        return Result.success()
    }
}
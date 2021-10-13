package com.lucifer.newsapplication

import android.app.Application
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.jakewharton.threetenabp.AndroidThreeTen
import com.lucifer.newsapplication.background.NewsWorker
import com.lucifer.newsapplication.db.NewsDatabase
import com.lucifer.newsapplication.network.RetrofitHelper
import com.lucifer.newsapplication.network.RetrofitService
import com.lucifer.newsapplication.repository.MainRepository
import com.lucifer.newsapplication.utils.PreferenceProvider
import java.util.concurrent.TimeUnit

class MainApplication : Application() {
    lateinit var repository: MainRepository

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        initialize()
        setupWorker()
    }

    private fun setupWorker() {
        val constraint = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        val workerRequest = PeriodicWorkRequest.Builder(NewsWorker::class.java, 5, TimeUnit.MINUTES)
            .setConstraints(constraint)
            .build()
        WorkManager.getInstance(this).enqueue(workerRequest)

    }

    // as we have to initialize repository again and again so I have initialized it here to make code clean
    private fun initialize() {
        val apiService = RetrofitHelper.getInstance().create(RetrofitService::class.java)
        val database = NewsDatabase.getDatabase(applicationContext)
        val pref = PreferenceProvider(applicationContext)
        repository = MainRepository(apiService, database, applicationContext, pref)
    }

}
package com.lucifer.newsapplication

import android.app.Application
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.jakewharton.threetenabp.AndroidThreeTen
import com.lucifer.newsapplication.background.NewsWorker
import com.lucifer.newsapplication.di.AppModule
import com.lucifer.newsapplication.di.DaggerAppComponent
import com.lucifer.newsapplication.repository.MainRepository
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainApplication : Application(), HasAndroidInjector {
    @Inject
    lateinit var mInjector: DispatchingAndroidInjector<Any>

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
        DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
            .inject(this)
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return mInjector
    }

}
package com.lucifer.newsapplication

import android.app.Application
import com.lucifer.newsapplication.db.NewsDatabase
import com.lucifer.newsapplication.network.RetrofitHelper
import com.lucifer.newsapplication.network.RetrofitService
import com.lucifer.newsapplication.repository.MainRepository

class MainApplication : Application() {
    lateinit var repository: MainRepository

    override fun onCreate() {
        super.onCreate()
        initialize()
    }

    // as we have to initialize repository again and again so I have initialized it here to make code clean
    private fun initialize() {
        val apiService = RetrofitHelper.getInstance().create(RetrofitService::class.java)
        val database = NewsDatabase.getDatabase(applicationContext)
        repository = MainRepository(apiService, database, applicationContext)
    }

}
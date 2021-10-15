package com.lucifer.newsapplication

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import com.lucifer.newsapplication.di.AppModule
import com.lucifer.newsapplication.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class MainApplication : Application(), HasAndroidInjector {
    @Inject
    lateinit var mInjector: DispatchingAndroidInjector<Any>


    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        initialize()
    }

    // used to initialize dagger
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
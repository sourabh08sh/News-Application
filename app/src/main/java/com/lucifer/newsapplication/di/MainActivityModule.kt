package com.lucifer.newsapplication.di

import com.lucifer.newsapplication.MainActivity
import com.lucifer.newsapplication.background.BackgroundService
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainActivityModule {

    @ContributesAndroidInjector
    abstract fun contributeMainActivityInjector() : MainActivity

    @ContributesAndroidInjector
    abstract fun contributeServiceInjector() : BackgroundService

}
package com.lucifer.newsapplication.di

import com.lucifer.newsapplication.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainActivityModule {

    @ContributesAndroidInjector
    abstract fun contributeMainActivityInjector() : MainActivity
}
package com.lucifer.newsapplication.di

import com.lucifer.newsapplication.MainApplication
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

// di module contains all the dagger classes that are used.

@Singleton
@Component(
    modules = [
    AndroidInjectionModule::class,
    MainActivityModule::class,
    AppModule::class
])
interface AppComponent {
    fun inject(application: MainApplication)
}
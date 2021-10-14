package com.lucifer.newsapplication.di

import android.content.Context
import com.lucifer.newsapplication.db.NewsDatabase
import com.lucifer.newsapplication.network.RetrofitHelper
import com.lucifer.newsapplication.network.RetrofitService
import com.lucifer.newsapplication.utils.PreferenceProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(
    private val context: Context
) {

    @Singleton
    @Provides
    fun provideRetrofitService(retrofitHelper: RetrofitHelper): RetrofitService {
        return retrofitHelper.getInstance().create(RetrofitService::class.java)
    }

    @Singleton
    @Provides
    fun providePreferences(): PreferenceProvider {
        return PreferenceProvider(context)
    }

    @Singleton
    @Provides
    fun provideDatabase(): NewsDatabase {
        return NewsDatabase.getDatabase(context)
    }

    @Singleton
    @Provides
    fun provideContext(): Context {
        return context
    }

}
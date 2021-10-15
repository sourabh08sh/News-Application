package com.lucifer.newsapplication.repository

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lucifer.newsapplication.MainActivity
import com.lucifer.newsapplication.R
import com.lucifer.newsapplication.db.NewsDatabase
import com.lucifer.newsapplication.models.Article
import com.lucifer.newsapplication.models.News
import com.lucifer.newsapplication.network.RetrofitService
import com.lucifer.newsapplication.utils.Coroutines
import com.lucifer.newsapplication.utils.NetworkUtils
import com.lucifer.newsapplication.utils.PreferenceProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.threeten.bp.LocalDateTime
import org.threeten.bp.temporal.ChronoUnit
import java.lang.Exception
import javax.inject.Inject

/* developed repository to handle data fetching from network and local DB and it provides data to view model.
* it holds references to data source to execute functions for accessing data.*/

class MainRepository @Inject constructor(
    private val apiService: RetrofitService,
    private val newsDatabase: NewsDatabase,
    private val applicationContext: Context,
    private val prefs: PreferenceProvider
) {
    // developed a mutable live data so we can do changes and as it is a live data we will get to know whenever a change happen.
    private val newsLiveData = MutableLiveData<Response<News>>()

    // declaring variables
    lateinit var notificationChannel: NotificationChannel
    private lateinit var builder: Notification.Builder
    private val channelId = "i.apps.notifications"
    private val description = "Test notification"

    init {
        newsLiveData.observeForever {
            saveArticleDb(it)
        }
    }

    private fun saveArticleDb(response: Response<News>) {
        Log.d("repo", response.data!!.articles.toString())
        Coroutines.io {
            prefs.saveLastSavedAt(LocalDateTime.now().toString())
            newsDatabase.newsDao().addArticles(response.data.articles)
        }
    }

    suspend fun getNews(): LiveData<List<Article>> {
        return withContext(Dispatchers.IO) {
            fetchNews("in", "apiKey", 50)
            newsDatabase.newsDao().getArticles()
        }
    }

    // function to get news data from server.
    private suspend fun fetchNews(country: String, apiKey:String, pageSize:Int){
        if (NetworkUtils.isInternetAvailable(applicationContext)){
            val lastSavedAt = prefs.getLastSavedAt()
            if (lastSavedAt == null || isFetchNeeded(LocalDateTime.parse(lastSavedAt))){
                try {
                    val result = apiService.getNews(country, apiKey, pageSize)
                    if (result.body() != null){
                        Log.d("repoApi", result.body()!!.articles.toString())
                        newsLiveData.postValue(Response.Success(result.body()))
                    } else{
                        Log.d("repoApi", result.body()!!.articles.toString())
                        newsLiveData.postValue(Response.Error("API Error"))
                    }
                }
                catch (e: Exception){
                    Log.d("repoApi", e.message.toString())
                    newsLiveData.postValue(Response.Error(e.message.toString()))
                }
            }
        }
    }

    private fun isFetchNeeded(savedAt: LocalDateTime): Boolean {
        return ChronoUnit.MINUTES.between(savedAt, LocalDateTime.now()) > 5
    }

    suspend fun getNewsBackground(notificationManager: NotificationManager) {
        showNotification(applicationContext, notificationManager)
        try {
            val result = apiService.getNews("in", "apiKey", 50)
            if (result.body() != null){
                Log.d("repoApi", result.body()!!.articles.toString())
                newsLiveData.postValue(Response.Success(result.body()))
            } else{
                Log.d("repoApi", result.body()!!.articles.toString())
                newsLiveData.postValue(Response.Error("API Error"))
            }
        }
        catch (e: Exception){
            Log.d("repoApi", e.message.toString())
            newsLiveData.postValue(Response.Error(e.message.toString()))
        }
    }

    private fun showNotification(context: Context, notificationManager: NotificationManager) {
        // Create an explicit intent for an Activity in your app
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        // checking if android version is greater than oreo(API 26) or not
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(false)
            // Register the channel with the system
            notificationManager.createNotificationChannel(notificationChannel)

            builder = Notification.Builder(context, channelId)
                .setContentTitle("Recent News")
                .setContentText("News have been refreshed.")
                .setSmallIcon(R.drawable.notification_icon)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
        } else {
            builder = Notification.Builder(context)
                .setContentTitle("Recent News")
                .setContentText("News have been refreshed.")
                .setSmallIcon(R.drawable.notification_icon)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
        }
        notificationManager.notify(1234, builder.build())
    }
}
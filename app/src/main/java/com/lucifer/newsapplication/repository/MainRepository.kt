package com.lucifer.newsapplication.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

/* developed repository to handle data fetching from network and local DB and it provides data to view model.
* it holds references to data source to execute functions for accessing data.*/

class MainRepository(
    private val apiService: RetrofitService,
    private val newsDatabase: NewsDatabase,
    private val applicationContext: Context,
    private val prefs: PreferenceProvider
) {
    // developed a mutable live data so we can do changes and as it is a live data we will get to know whenever a change happen.
    private val newsLiveData = MutableLiveData<Response<News>>()

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
            fetchNews("in", "6b1a0b77fee444ba9f4f35ec635f9129", 50)
            newsDatabase.newsDao().getArticles()
        }
    }

    // function to get news data
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

    suspend fun getNewsBackground(){
        try {
            val result = apiService.getNews("in", "6b1a0b77fee444ba9f4f35ec635f9129", 50)
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
package com.lucifer.newsapplication.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.lucifer.newsapplication.db.NewsDatabase
import com.lucifer.newsapplication.models.Article
import com.lucifer.newsapplication.models.News
import com.lucifer.newsapplication.network.RetrofitService
import com.lucifer.newsapplication.utils.Coroutines
import com.lucifer.newsapplication.utils.NetworkUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

/* developed repository to handle data fetching from network and local DB and it provides data to view model.
* it holds references to data source to execute functions for accessing data.*/

class MainRepository(
    private val apiService: RetrofitService,
    private val newsDatabase: NewsDatabase,
    private val applicationContext: Context
) {
    // developed a mutable live data so we can do changes and as it is a live data we will get to know whenever a change happen.
    private val newsLiveData = MutableLiveData<Response<News>>()

    private val news: LiveData<Response<News>>
        get() = newsLiveData

    init {
        news.observeForever {
            saveArticleDb(it)
        }
    }

    private fun saveArticleDb(response: Response<News>?) {
        Coroutines.io {
            newsDatabase.newsDao().addArticles(response!!.data!!.articles)
        }
    }

    suspend fun getNews(country: String, apiKey:String): LiveData<List<Article>> {
        return withContext(Dispatchers.IO) {
            fetchNews(country, apiKey)
            newsDatabase.newsDao().getArticles()
        }
    }

    // function to get marvel character data
    private suspend fun fetchNews(country: String, apiKey:String){
        if (NetworkUtils.isInternetAvailable(applicationContext)){
            try {
                val result = apiService.getNews(country, apiKey)
                if (result.body() != null){
                    newsLiveData.postValue(Response.Success(result.body()))
                } else{
                    newsLiveData.postValue(Response.Error("API Error"))
                }
            }
            catch (e: Exception){
                newsLiveData.postValue(Response.Error(e.message.toString()))
            }
        }
    }
}
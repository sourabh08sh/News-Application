package com.lucifer.newsapplication.network

import com.lucifer.newsapplication.models.News
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitService {
    // these are input operations so we don't want them to run on main thread. So for running them on background thread we are using suspend.

    @GET("top-headlines")
    suspend fun getNews(@Query("country") country:String,
                        @Query("apiKey") offset:String) : Response<News>

}
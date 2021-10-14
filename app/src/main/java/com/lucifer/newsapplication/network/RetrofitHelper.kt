package com.lucifer.newsapplication.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class RetrofitHelper @Inject constructor(){

    companion object{
        private const val BASE_URL = "https://newsapi.org/v2/"
    }

    fun getInstance() : Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

}
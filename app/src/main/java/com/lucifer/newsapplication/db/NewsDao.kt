package com.lucifer.newsapplication.db

import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lucifer.newsapplication.models.Article

interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addArticles(articles: List<Article>)

    @Query("SELECT * FROM articles_table")
    suspend fun getArticles(): LiveData<List<Article>>
}
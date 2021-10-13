package com.lucifer.newsapplication.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.lucifer.newsapplication.models.Article

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(articles: List<Article>)

    @Query("DELETE FROM articles_table where publishedAt NOT IN (SELECT publishedAt from articles_table ORDER BY datetime(publishedAt) DESC LIMIT 50)")
    fun delete()

    @Transaction
    fun addArticles(articles: List<Article>){
        insert(articles)
        delete()
    }

    @Query("SELECT * FROM articles_table ORDER BY datetime(publishedAt) DESC")
    fun getArticles(): LiveData<List<Article>>
}
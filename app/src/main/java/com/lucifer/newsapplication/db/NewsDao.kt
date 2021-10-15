package com.lucifer.newsapplication.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.lucifer.newsapplication.models.Article

@Dao
interface NewsDao {

    // this function will insert list of articles in database
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(articles: List<Article>)

    // this function will delete list of articles in descending order it will keep first 50 and delete the rest.
    @Query("DELETE FROM articles_table where publishedAt NOT IN (SELECT publishedAt from articles_table ORDER BY datetime(publishedAt) DESC LIMIT 50)")
    fun delete()

    // this function helps to call both insert and delete function at one time.
    @Transaction
    fun addArticles(articles: List<Article>){
        insert(articles)
        delete()
    }

    // this function is used to get all articles from database.
    @Query("SELECT * FROM articles_table ORDER BY datetime(publishedAt) DESC")
    fun getArticles(): LiveData<List<Article>>
}
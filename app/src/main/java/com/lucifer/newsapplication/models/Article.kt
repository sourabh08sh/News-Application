package com.lucifer.newsapplication.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.lucifer.newsapplication.utils.Converters

@Entity(tableName = "articles_table", indices = [Index(value = ["title"],
    unique = true)])
data class Article(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val description: String?,
    @TypeConverters(Converters::class)
    val publishedAt: String,
    val title: String?,
    val url: String?,
    val urlToImage: String?
)
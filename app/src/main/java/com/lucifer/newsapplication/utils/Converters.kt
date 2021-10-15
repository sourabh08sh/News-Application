package com.lucifer.newsapplication.utils

import androidx.room.TypeConverter
import java.util.*

// it used to covert type of date because we cannot save date type directly into room db.
class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }
}
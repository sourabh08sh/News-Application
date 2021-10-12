package com.lucifer.newsapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucifer.newsapplication.models.News
import com.lucifer.newsapplication.repository.MainRepository
import com.lucifer.newsapplication.repository.Response
import com.lucifer.newsapplication.utils.lazyDeferred
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewsViewModel(private val repository: MainRepository) : ViewModel() {

    @DelicateCoroutinesApi
    val news by lazyDeferred {
        repository.getNews("in", "6b1a0b77fee444ba9f4f35ec635f9129")
    }

}
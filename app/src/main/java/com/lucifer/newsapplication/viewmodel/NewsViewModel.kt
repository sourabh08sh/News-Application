package com.lucifer.newsapplication.viewmodel

import androidx.lifecycle.ViewModel
import com.lucifer.newsapplication.repository.MainRepository
import com.lucifer.newsapplication.utils.lazyDeferred
import kotlinx.coroutines.DelicateCoroutinesApi
import javax.inject.Inject

class NewsViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {

    @DelicateCoroutinesApi
    val news by lazyDeferred {
        repository.getNews()
    }

}
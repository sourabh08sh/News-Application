package com.lucifer.newsapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.lucifer.newsapplication.utils.Coroutines
import com.lucifer.newsapplication.viewmodel.NewsViewModel
import com.lucifer.newsapplication.viewmodel.NewsViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@DelicateCoroutinesApi
class MainActivity : AppCompatActivity() {
    lateinit var newsViewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val repository = (application as MainApplication).repository

        newsViewModel = ViewModelProvider(this, NewsViewModelFactory(repository)).get(NewsViewModel::class.java)

        bindUI()
    }

    private fun bindUI() = Coroutines.main {
        newsViewModel.news.await().observe(this, Observer {
            Log.d("NEWSDATA", it.toString())
            Toast.makeText(this@MainActivity, it.size.toString(), Toast.LENGTH_SHORT).show()
        })
    }
}
package com.lucifer.newsapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import com.lucifer.newsapplication.utils.Coroutines
import com.lucifer.newsapplication.viewmodel.NewsViewModel
import dagger.android.AndroidInjection
import kotlinx.coroutines.DelicateCoroutinesApi
import javax.inject.Inject

@DelicateCoroutinesApi
class MainActivity : AppCompatActivity() {
    @Inject lateinit var newsViewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindUI()
    }

    private fun bindUI() = Coroutines.main {
        newsViewModel.news.await().observe(this, Observer {
            Log.d("NEWSDATA", it.toString())
            Toast.makeText(this@MainActivity, it.size.toString(), Toast.LENGTH_SHORT).show()
        })
    }
}
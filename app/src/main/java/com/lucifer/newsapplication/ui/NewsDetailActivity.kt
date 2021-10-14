package com.lucifer.newsapplication.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import com.lucifer.newsapplication.R

class NewsDetailActivity : AppCompatActivity() {

    private var newsLink : String = ""

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_detail)

        newsLink = intent.getStringExtra("url").toString()

        val webView: WebView = findViewById(R.id.webView)
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url.toString())
                return true
            }
        }
        webView.settings.javaScriptEnabled = true
        webView.loadUrl(newsLink)
    }
}
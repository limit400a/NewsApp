package com.example.newsapp.ui.detail

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.newsapp.R
//接收新闻URL，用WebView加载网页。
class DetailActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val title = intent.getStringExtra("title") ?: ""
        val url = intent.getStringExtra("url") ?: ""

        findViewById<TextView>(R.id.detailTitle).text = title
        val webView = findViewById<WebView>(R.id.webView)
        webView.webViewClient = WebViewClient()         //在App内打开链接

        webView.loadUrl(url)
    }
}
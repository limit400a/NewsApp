package com.example.newsapp.data

// 新闻数据类，对应API返回的单条新闻
data class News(
    val title: String,      // 新闻标题
    val author: String,     // 作者名称
    val url: String,        // 新闻链接（最后一个非空属性，后面无逗号）
    val imageUrl: String?   // 图片链接（可为空，类型带问号）
)

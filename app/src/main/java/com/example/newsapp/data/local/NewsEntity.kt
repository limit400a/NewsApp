package com.example.newsapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

// Room数据库表：收藏的新闻
@Entity(tableName = "collected_news")
data class NewsEntity (

    @PrimaryKey val url: String,        // 主键：新闻链接（唯一）
    val tittle: String,     // 标题
    val author: String?,     // 作者
    val imageUrl: String?,  // 图片
    val collectTime: Long = System.currentTimeMillis()    //收藏时间
)
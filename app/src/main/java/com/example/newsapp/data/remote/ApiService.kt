package com.example.newsapp.data.remote

import com.example.newsapp.data.News
import retrofit2.http.GET
import retrofit2.http.Query

// Retrofit网络接口，定义新闻API请求
interface ApiService {

    // 获取新闻列表（挂起函数，支持协程）
    @GET("toutiao/index")
    suspend fun getNews(
        @Query("key") apiKey:String,         // API密钥参数
        @Query("type") type:String = "top"  // 新闻类型，默认"top"（头条）
    ) : NewsResponse                        // 返回包装后的响应对象
}

// API响应包装类（最外层）
data class NewsResponse(
    val result : ResultDate?     // result字段可能为空
)

// 实际数据层（包含新闻列表）
data class ResultDate(
    val data : List<News>?      // data字段是新闻列表，可能为空
)
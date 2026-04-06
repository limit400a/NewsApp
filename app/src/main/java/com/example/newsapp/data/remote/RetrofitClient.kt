package com.example.newsapp.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
//RetrofitClient是一个"网络连接工厂"，它负责创建和管理Retrofit实例，让App能通过HTTP协议调用新闻API接口。用单例模式（object）保证整个App只创建一个实例，避免重复初始化。
// Retrofit客户端单例，提供ApiService实例
object RetrofitClient {
    // 聚合数据API的基础地址
    private const val BASE_URL = "http://v.juhe.cn/"

    // 懒加载创建Retrofit实例
    private val retrofit : Retrofit by lazy{
        Retrofit.Builder()
            .baseUrl(BASE_URL)                                      // 设置基础URL
            .addConverterFactory(GsonConverterFactory.create())     // 添加Gson解析器
            .build()
    }
    // 提供ApiService实例
    val apiService : ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}
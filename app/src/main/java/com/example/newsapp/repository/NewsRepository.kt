package com.example.newsapp.repository

import android.content.Context
import android.util.Log
import com.example.newsapp.data.News
import com.example.newsapp.data.local.AppDatabase
import com.example.newsapp.data.local.NewsEntity
import com.example.newsapp.data.remote.RetrofitClient

//Repository是"数据中转站"，它把网络请求（Retrofit）和本地数据库（后续Room）统一封装起来。UI层（Activity）不直接调用网络，而是通过Repository拿数据，这样网络换成本地、或者换API接口时，UI代码不用改。
//NewsRepository是App的"数据管家"，它把网络请求的细节（Retrofit调用、错误处理、空数据判断）封装起来。UI层只需要调用getNews()，不用关心数据从哪来、网络断了怎么办。

// 新闻数据仓库，封装网络请求和错误处理
class NewsRepository(context: Context){

    // 从RetrofitClient获取ApiService实例
    private val apiService = RetrofitClient.apiService
    private val newsDao = AppDatabase.getInstance(context).newsDao()

    // 获取新闻列表（挂起函数，在协程中调用）
    suspend fun getNews(apiKey:String):Result<List<News>>{
        return try {

            //调用接口
            val repository = apiService.getNews(apiKey = apiKey)
            //提取新闻列表(如果data为空，返回空列表)
            val newsList = repository.result?.data?:emptyList()
            //返回成功结果
            Result.success(newsList)
        }catch (e:Exception){
            //网络失败，打印错误日志
            Log.e("NewsRepository","获取信息失败:${e.message}")
            //返回失败结果
            Result.failure(e);
        }
    }

    //收藏新闻
    suspend fun collectNews(news: News){
        val entity = NewsEntity(
            url = news.url,
            tittle = news.title,
            author = news.author,
            imageUrl = news.imageUrl
        )
        newsDao.insert(entity)
    }
    //获取所有收藏
    suspend fun getCollectNews():List<News>{
        return newsDao.getAllCollected().map {
            entity ->
            News(
                title = entity.tittle,
                author = entity.author,
                url = entity.url,
                imageUrl = entity.imageUrl
            )
        }
    }
    //是否已收藏
    suspend fun isCollected(url:String):Boolean{
        return newsDao.isCollected(url)!=null
    }
    //取消收藏
    suspend fun removeCollect(news: News){
        val entity =NewsEntity(
            url = news.url,
            tittle = news.title,
            author = news.author,
            imageUrl = news.imageUrl
        )
        newsDao.delete(entity)
    }
}
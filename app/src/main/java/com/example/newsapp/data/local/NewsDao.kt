package com.example.newsapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

// @Dao：标记为数据访问接口
@Dao
interface NewsDao {
    //查询所有收藏
    @Query("SELECT * FROM collected_news ORDER BY collectTime DESC")
    suspend fun getAllCollected():List<NewsEntity>

    //插入收藏（如果已存在则替换）
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(news:NewsEntity)

    //删除收藏
    @Delete
    suspend fun delete(news: NewsEntity)

    //根据URL查询是否已收藏
    @Query("SELECT * FROM collected_news WHERE url = :url")
    suspend fun isCollected(url:String): NewsEntity?
}
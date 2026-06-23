package com.example.newsapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
//1. @Database：标记为Room数据库
// entities：关联所有实体表；version：数据库版本；exportSchema：导出数据库结构文件
@Database(entities = [NewsEntity::class], version = 1)

abstract class AppDatabase :RoomDatabase(){
    //2.抽象方法：返回DAO，Room自动实现
    abstract fun newsDao():NewsDao
    // 3. 单例模式：全局唯一数据库实例
    companion object{
        //Volatile 保证多线程可见
        @Volatile
        private var INSTANCE : AppDatabase? = null
        fun getInstance(context: Context):AppDatabase{
            return INSTANCE ?: synchronized(this){
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,// 用应用上下文，避免内存泄漏
                    AppDatabase::class.java,
                    "news_database"// 数据库文件名（本地沙箱文件）
                ).build().also { INSTANCE =it }
            }
        }
    }
}
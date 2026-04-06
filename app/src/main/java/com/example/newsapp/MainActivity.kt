package com.example.newsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.example.newsapp.repository.NewsRepository
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    //创建NewsRepository实例
    private val repository = NewsRepository();

    //你的API Key（实际开发中应该放在配置文件里，不要硬编码）
    private val AppKey ="6cc8a8a567ea96eeede2b6b80b01eff4"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val textView = findViewById<TextView>(R.id.textView)
//        val test = "我能写Kotlin了"
        textView.text = "正在加载新闻..."
        //在协程中调用网络请求（lifecycleScope自动管理生命周期）
        lifecycleScope.launch {
            val result = repository.getNews(AppKey)
            result.onSuccess {
                newsList ->
                //成功：显示第一条新闻标题
                val firstNews = newsList.firstOrNull()
                val displayText = firstNews?.title?:"没有新闻数据"
                textView.text = displayText

                // 在Logcat里打印所有新闻标题（调试用）
                newsList.forEach { news ->
                    Log.d("MainActivity", "新闻:${news.title}")
                }

            }.onFailure {
                // 失败：显示错误信息
                error ->
                textView.text = "加载失败：${error.message}"
                Log.e("MainActivity","请求失败",error)
            }
        }
    }
}
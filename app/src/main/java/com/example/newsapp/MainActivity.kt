package com.example.newsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.repository.NewsRepository
import com.example.newsapp.ui.NewsAdapter
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    //创建NewsRepository实例和Adapter实例
    private val repository = NewsRepository();
    private val newsAdapter = NewsAdapter()

    //你的API Key（实际开发中应该放在配置文件里，不要硬编码）
    private val AppKey ="6cc8a8a567ea96eeede2b6b80b01eff4"

    //UI控件
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var emptyView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        //初始化控件
        recyclerView = findViewById(R.id.recyclerView)
        progressBar = findViewById(R.id.progressBar)
        emptyView = findViewById(R.id.emptyView)

        //设置RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)  //线性排列
        recyclerView.adapter = newsAdapter  //绑定适配器

        //加载新闻数据
        loadNews()
    }
    //加载新闻数据
    private fun loadNews(){
        //显示加载中
        showloading()
        //
        lifecycleScope.launch {
            val result = repository.getNews(AppKey)
            result.onSuccess { newsList ->
                if (newsList.isEmpty()){
                    //数据为空
                    showEmpty()
                }else{
                    //显示列表
                    showList(newsList)
                }
            }.onFailure { error ->
                //显示错误
                showError(error.message ?:"加载失败")
            }
        }
    }
    //显示加载中
    private fun showloading(){
        progressBar.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
        emptyView.visibility  =View.GONE
    }
    //显示列表状态
    private fun showList(newsList: List<com.example.newsapp.data.News>){
        progressBar.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
        emptyView.visibility = View.GONE

        newsAdapter.submitList(newsList)    //更新Adapter数据
    }
    //显示空数据状态
    private fun showEmpty(){
        progressBar.visibility = View.GONE
        recyclerView.visibility = View.GONE
        emptyView.visibility = View.VISIBLE
        emptyView.text = "暂无新闻消息"
    }

    //显示错误状态
    private fun showError(message: String){
        progressBar.visibility = View.GONE
        recyclerView.visibility = View.GONE
        emptyView.visibility = View.VISIBLE
        emptyView.text = "加载失败 : $message"
    }
}
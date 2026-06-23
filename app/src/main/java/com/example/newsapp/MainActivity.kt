package com.example.newsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.repository.NewsRepository
import com.example.newsapp.ui.NewsAdapter
import com.example.newsapp.ui.news.NewsViewModel
import com.example.newsapp.ui.news.ViewModelFactory
import kotlinx.coroutines.launch

private lateinit var swipeRefresh:
        androidx.swiperefreshlayout.widget.SwipeRefreshLayout
class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: NewsViewModel

    //创建NewsRepository实例和Adapter实例
    private val repository by lazy { NewsRepository(this) }
    private val newsAdapter = NewsAdapter()

    //API Key
    private val AppKey ="6cc8a8a567ea96eeede2b6b80b01eff4"

    //UI控件
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var emptyView: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //初始化控件
        recyclerView = findViewById(R.id.recyclerView)
        progressBar = findViewById(R.id.progressBar)
        emptyView = findViewById(R.id.emptyView)
        swipeRefresh = findViewById(R.id.swipeRefresh)

        //初始化ViewModel
        val repository = NewsRepository(this)
        viewModel = ViewModelProvider(this, ViewModelFactory(repository)).get(NewsViewModel::class.java)

        //观察LiveData
        viewModel.newsList.observe(this){newsList ->
            if (newsList.isEmpty()) showEmpty() else showList(newsList)
        }

        viewModel.isLoading.observe(this){isLoading ->
            if (isLoading) showLoading() else hideLoading()
        }

        viewModel.error.observe(this){ error ->
            error?.let { showError(it) }
        }

        //加载数据
        viewModel.loadNews(AppKey)

        //下拉刷新
        swipeRefresh.setOnRefreshListener {
            viewModel.loadNews(AppKey)
        }

        //设置RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)  //线性排列
        recyclerView.adapter = newsAdapter  //绑定适配器
        //收藏回调
        newsAdapter.onCollectClick = {news ->
            lifecycleScope.launch {
                repository.collectNews(news)
                Toast.makeText(this@MainActivity,"已收藏",Toast.LENGTH_SHORT).show()
            }
        }

//        //初始化SwipeRefresh
//        swipeRefresh = findViewById(R.id.swipeRefresh)
//
//        //设置下拉刷新
//        swipeRefresh.setOnRefreshListener {
//            //加载新闻数据
//            loadNews()
//        }
//        // 加载新闻数据
//        loadNews()

    }


    //加载新闻数据
    private fun loadNews(){
        //显示加载中
        showLoading()
        //
        lifecycleScope.launch {
            val result = repository.getNews(AppKey)
            result.onSuccess { newsList ->
                if (newsList.isEmpty()){
                    //数据为空
                    showEmpty()
                }else{
                    //显示列表S
                    showList(newsList)
                }
            }.onFailure { error ->
                //显示错误
                showError(error.message ?:"加载失败")
            }
        }
    }
    //显示加载中，如果是下拉刷新触发的加载，不显示ProgressBar
    private fun showLoading(){
        if (!swipeRefresh.isRefreshing){
            progressBar.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        }
        emptyView.visibility  =View.GONE
    }
    private fun hideLoading(){
        progressBar.visibility = View.GONE
        swipeRefresh.visibility = View.VISIBLE
        emptyView.visibility = View.GONE
        swipeRefresh.isRefreshing = false
    }
    //显示列表状态
    private fun showList(newsList: List<com.example.newsapp.data.News>){
        progressBar.visibility = View.GONE
        swipeRefresh.visibility = View.VISIBLE  // 显示 SwipeRefreshLayout
        recyclerView.visibility = View.VISIBLE
        emptyView.visibility = View.GONE

        swipeRefresh.isRefreshing = false // 隐藏刷新动画

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
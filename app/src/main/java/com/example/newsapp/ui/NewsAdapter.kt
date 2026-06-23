package com.example.newsapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.newsapp.R
import com.example.newsapp.data.News

// RecyclerView适配器，负责把新闻数据绑定到列表项视图
//创建了NewsAdapter，它负责把新闻数据列表转换成RecyclerView的列表项。包含三个核心方法：创建ViewHolder、绑定数据、返回总数。ViewHolder缓存视图引用，避免滑动时重复查找控件。
class NewsAdapter : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>(){
    //新闻数据列表
    private  var newsList : List<News> = emptyList()

    //收藏回调
    var onCollectClick:((News)->Unit) ?= null
    //更新数据的方法(外部调用)
    fun submitList(list: List<News>){
        newsList = list
        notifyDataSetChanged()      //通知RecyclerView刷新
    }
    //创建ViewHolder（当列表需要新的像是调用）
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        //加载item_news.xml
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_news,parent,false)
        return NewsViewHolder(view)
    }
    //返回列表项总数
    override fun getItemCount(): Int =newsList.size

    //绑定数据到ViewHolder（当项需要显示时调用）
    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val news = newsList[position]
        holder.bind(news)

        //新闻点击跳转详情
        holder.itemView.setOnClickListener {
            val intent = android.content.Intent(holder.itemView.context,com.example.newsapp.ui.detail.DetailActivity::class.java)
            intent.putExtra("title",news.title)
            intent.putExtra("url",news.url)
            holder.itemView.context.startActivity(intent)
        }
        //收藏按钮点击
        holder.itemView.findViewById<ImageView>(R.id.collectBtn).setOnClickListener {
            onCollectClick?.invoke(news)
        }
    }
    //ViewHolder:缓存每个列表项的视图引用，避免重复findViewById
    inner class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val titleText : TextView = itemView.findViewById(R.id.newsTitle)
        private val authorText: TextView = itemView.findViewById(R.id.newsAuthor)
        private val imageView : ImageView = itemView.findViewById(R.id.newsImage)
        private val collectBtn:ImageView = itemView.findViewById(R.id.collectBtn)
        fun bind(news: News){
            titleText.text = news.title
            authorText.text = news.author

            //Coil是Kotlin专用的图片加载库，能异步加载网络图片并显示在ImageView里
            news.imageUrl?.let { url ->
                if (url.isEmpty()){
                    imageView.load(url){
                        placeholder(R.drawable.ic_launcher_background)      // 加载中占位图
                        error(R.drawable.ic_launcher_background)            // 加载失败占位图
                    }
                }
            }
//            //设置收藏图标状态
//            collectBtn.setImageResource(
//                if (isCollected) R.drawable.collected   //已收藏
//                else R.drawable.not_collect             //未收藏
//            )
//            collectBtn.setOnClickListener {
//                onCollectClick(news,!isCollected)
//            }
            //默认显示未收藏图标
            collectBtn.setImageResource(R.drawable.not_collect)
        }
    }
}
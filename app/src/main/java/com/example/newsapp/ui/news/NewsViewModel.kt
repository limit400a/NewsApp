package com.example.newsapp.ui.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.News
import com.example.newsapp.repository.NewsRepository
import kotlinx.coroutines.launch

class NewsViewModel(private val repository: NewsRepository): ViewModel() {
    private val _newsList = MutableLiveData<List<News>>()
    val newsList : LiveData<List<News>> = _newsList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error : LiveData<String?> = _error

    fun loadNews(apiKey:String){
        _isLoading.value = true
        _error.value = null

        viewModelScope.launch {
            val result = repository.getNews(apiKey)

            result.onSuccess { newsList ->
                _newsList.value = newsList
            }
            result.onFailure { error->
                _error.value = error.message ?: "加载失败"
            }
            _isLoading.value = false
        }
    }
}
package com.example.androidbootcampturkey.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidbootcampturkey.model.Post
import com.example.androidbootcampturkey.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel(private val repository: Repository) : ViewModel() {
    val myResponse: MutableLiveData<Response<Post>> = MutableLiveData()

    fun getPost() {
        viewModelScope.launch(Dispatchers.Main) {
            val response: Response<Post> = repository.getPost()
            myResponse.value = response
        }
    }

}
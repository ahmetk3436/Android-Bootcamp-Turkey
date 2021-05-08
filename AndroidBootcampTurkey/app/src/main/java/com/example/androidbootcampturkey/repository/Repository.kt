package com.example.androidbootcampturkey.repository

import com.example.androidbootcampturkey.model.Post
import com.example.androidbootcampturkey.util.RetrofitInstance
import retrofit2.Response

class Repository {
    suspend fun getPost(): Response<Post> {
        return RetrofitInstance.api.getPost()
    }
}
package com.example.androidbootcampturkey.api

import com.example.androidbootcampturkey.model.Post
import retrofit2.Response
import retrofit2.http.GET

interface SimpleApi {

    @GET("latest?base=TRY")
    suspend fun getPost(): Response<Post>

}
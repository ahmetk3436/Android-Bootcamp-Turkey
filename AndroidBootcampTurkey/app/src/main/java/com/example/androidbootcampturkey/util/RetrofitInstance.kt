package com.example.androidbootcampturkey.util

import com.example.androidbootcampturkey.api.SimpleApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val retrofit by lazy {
        val base_url = "https://api.ratesapi.io/api/"
        Retrofit.Builder().baseUrl(base_url).addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: SimpleApi by lazy {
        retrofit.create(SimpleApi::class.java)
    }
}
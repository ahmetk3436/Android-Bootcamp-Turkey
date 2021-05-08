package com.example.androidbootcampturkey.model

import com.google.gson.annotations.SerializedName

data class Post(
    @SerializedName("rates")
    val data: Model
)
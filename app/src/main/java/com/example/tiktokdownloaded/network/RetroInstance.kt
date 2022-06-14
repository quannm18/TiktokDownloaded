package com.example.tiktokdownloaded.network

import com.example.tiktokdownloaded.util.Constants.Companion.BASE_URL
import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetroInstance {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }

    public val api : TikTokApi by lazy {
        retrofit.create(TikTokApi::class.java)
    }
}
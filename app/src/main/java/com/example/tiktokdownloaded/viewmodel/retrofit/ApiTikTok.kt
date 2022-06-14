package com.example.tiktokdownloaded.viewmodel.retrofit

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiTikTok {
    val url: String
        get() = "https://api2.musical.ly/aweme/v1/aweme/detail/?aweme_id=idVideo" as String
    var gson: Gson
        get() = GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create()
        set(value) = TODO()
    var apiTikTok: ApiTikTok
        get() = Retrofit.Builder()
            .baseUrl("https://api2.musical.ly/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiTikTok::class.java)
        set(value) = TODO()

    @POST("aweme/v1/aweme/detail/")
    fun getData(@Query("aweme_id") aweme_id:String): Call<String>
}
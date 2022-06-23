package com.example.tiktokdownloaded.network.repository

import com.example.tiktokdownloaded.model.TikTokModel
import com.example.tiktokdownloaded.network.RetroInstance
import retrofit2.Response

class Repository {
    suspend fun getPost(id: String): Response<TikTokModel> {
        return RetroInstance.api.getTiktok(id)
    }
}
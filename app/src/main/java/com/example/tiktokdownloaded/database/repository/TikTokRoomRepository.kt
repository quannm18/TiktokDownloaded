package com.example.tiktokdownloaded.database.repository

import androidx.lifecycle.LiveData
import com.example.tiktokdownloaded.database.dao.TikTokDAO
import com.example.tiktokdownloaded.model.TikTokEntity

class TikTokRoomRepository (private val tikTokDAO: TikTokDAO){
    val readAllData : LiveData<List<TikTokEntity>> = tikTokDAO.readData()

    fun addTikTok(tikTokEntity: TikTokEntity){
        tikTokDAO.addTikTokToDB(tikTokEntity)
    }
}
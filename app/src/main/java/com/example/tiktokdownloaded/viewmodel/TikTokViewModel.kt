package com.example.tiktokdownloaded.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.tiktokdownloaded.model.TikTokEntity
import com.example.tiktokdownloaded.database.repository.TikTokRoomRepository
import com.example.tiktokdownloaded.util.TikTokDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TikTokViewModel(application: Application) : AndroidViewModel(application) {

    val readAllData : LiveData<List<TikTokEntity>>
    private val tikTokRoomRepository : TikTokRoomRepository

    init {
        val mTikTokDAO = TikTokDatabase.getDatabase(application).tikTokDAO()

        tikTokRoomRepository = TikTokRoomRepository(mTikTokDAO)
        readAllData = tikTokRoomRepository.readAllData
    }

    fun addTikTok(tikTokEntity: TikTokEntity){
        viewModelScope.launch(Dispatchers.IO) {
            tikTokRoomRepository.addTikTok(tikTokEntity)
        }
    }

}
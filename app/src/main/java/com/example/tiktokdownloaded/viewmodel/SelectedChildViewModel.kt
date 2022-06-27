package com.example.tiktokdownloaded.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tiktokdownloaded.model.TikTokEntity

class SelectedChildViewModel : ViewModel() {
    private val mutableLiveData : MutableLiveData<TikTokEntity> = MutableLiveData()

    fun setTikTok(tikTokEntity: TikTokEntity){
        mutableLiveData.value = tikTokEntity
    }

    fun getTikTok(): MutableLiveData<TikTokEntity>{
        return mutableLiveData
    }
}
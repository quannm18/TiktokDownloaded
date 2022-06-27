package com.example.tiktokdownloaded.view.update_download

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tiktokdownloaded.model.TikTokEntity

class UpdateDownload : ViewModel() {
    public var totalData : MutableLiveData<Long> = MutableLiveData()

    fun setTotal(int: Long){
        totalData.postValue(int)
    }

    fun getTotal(): LiveData<Long> {
        return totalData
    }
}
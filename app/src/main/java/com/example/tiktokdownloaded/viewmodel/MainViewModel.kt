package com.example.tiktokdownloaded.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiktokdownloaded.model.TikTokModel
import com.example.tiktokdownloaded.repository.Repository
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel(private val repository: Repository) : ViewModel(){
    public val myResponse : MutableLiveData<Response<TikTokModel>> = MutableLiveData()

    fun getPostTikTok(id: String){
        viewModelScope.launch {
            val response = repository.getPost(id)
            myResponse.value = response
        }
    }
}
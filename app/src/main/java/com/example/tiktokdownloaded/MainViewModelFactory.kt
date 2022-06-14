package com.example.tiktokdownloaded

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tiktokdownloaded.repository.Repository
import com.example.tiktokdownloaded.viewmodel.MainViewModel
import retrofit2.Response

class MainViewModelFactory(val response: Repository) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(response) as T
    }



}
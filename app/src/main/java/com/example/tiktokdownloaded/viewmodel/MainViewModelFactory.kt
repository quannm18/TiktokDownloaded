package com.example.tiktokdownloaded.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tiktokdownloaded.network.repository.Repository

class MainViewModelFactory(val response: Repository) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(response) as T
    }



}
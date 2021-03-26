package com.littleboy.app.flomoex

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.littleboy.app.flomoex.network.FlomoRepository

class ViewModelFactory(private val repository: FlomoRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReceiveTextViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ReceiveTextViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
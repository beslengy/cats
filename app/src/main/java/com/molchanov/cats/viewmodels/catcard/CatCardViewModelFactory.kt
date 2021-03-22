package com.molchanov.cats.viewmodels.catcard

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CatCardViewModelFactory(
    private val imageId: String,
    private val application: Application) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CatCardViewModel::class.java)) {
            return CatCardViewModel(imageId, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
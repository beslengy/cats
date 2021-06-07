package com.molchanov.cats.viewmodels.catcard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.molchanov.cats.repository.CatsRepository

class CatCardViewModelFactory(
    private val repository: CatsRepository,
    private val imageId: String
    ) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CatCardViewModel::class.java)) {
            return CatCardViewModel(repository, imageId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
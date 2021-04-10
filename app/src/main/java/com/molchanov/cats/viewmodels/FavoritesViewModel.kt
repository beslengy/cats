package com.molchanov.cats.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.molchanov.cats.network.CatsApi
import com.molchanov.cats.network.NetworkFavorites
import kotlinx.coroutines.launch

class FavoritesViewModel : ViewModel(){

    private val _favoriteImages = MutableLiveData<List<NetworkFavorites>>()
    val favoriteImages: LiveData<List<NetworkFavorites>> get() = _favoriteImages

    private val _navigateToCard = MutableLiveData<NetworkFavorites>()
    val navigateToCard: LiveData<NetworkFavorites>
        get() = _navigateToCard

    init {
        getFavorites()
    }

    private fun getFavorites() {
        viewModelScope.launch {
            try {
                _favoriteImages.value = CatsApi.retrofitService.getAllFavorites()
            } catch (e: Exception) {

            }
        }
    }

    fun displayCatCard(currentImage: NetworkFavorites) {
        _navigateToCard.value = currentImage
    }
    fun displayCatCardComplete() {
        _navigateToCard.value = null
    }
}
package com.molchanov.cats.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.molchanov.cats.network.CatsApi
import com.molchanov.cats.network.NetworkFavorite
import com.molchanov.cats.utils.FAV_QUERY_OPTIONS
import kotlinx.coroutines.launch

class FavoritesViewModel : ViewModel(){

    private val _favoriteImages = MutableLiveData<List<NetworkFavorite>>()
    val favoriteImages: LiveData<List<NetworkFavorite>> get() = _favoriteImages

    private val _navigateToCard = MutableLiveData<NetworkFavorite>()
    val navigateToCard: LiveData<NetworkFavorite>
        get() = _navigateToCard

    init {
        getFavorites()
    }

    private fun getFavorites() {
        viewModelScope.launch {
            try {
                _favoriteImages.value = CatsApi.retrofitService.getAllFavorites(FAV_QUERY_OPTIONS)
            } catch (e: Exception) {

            }
        }
    }

    fun displayCatCard(currentImage: NetworkFavorite) {
        _navigateToCard.value = currentImage
    }
    fun displayCatCardComplete() {
        _navigateToCard.value = null
    }
}
package com.molchanov.cats.viewmodels.favorites

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.molchanov.cats.domain.Cat
import com.molchanov.cats.utils.REPOSITORY
import com.molchanov.cats.utils.showToast
import kotlinx.coroutines.launch

class FavoritesViewModel : ViewModel(){

    private val _favoriteImages = MutableLiveData<List<Cat>>()
    val favoriteImages: LiveData<List<Cat>> get() = _favoriteImages

    private val _navigateToCard = MutableLiveData<Cat>()
    val navigateToCard: LiveData<Cat>
        get() = _navigateToCard

    private val _response = MutableLiveData<String>()
    private val response: LiveData<String> get() = _response

    init {
        Log.d("M_FavoritesViewModel", "FavoritesViewModel инициализируется")
        getFavorites()
        Log.d("M_FavoritesViewModel", "getFavorites отработал")
    }

    private fun getFavorites() {
        viewModelScope.launch {
            try {
                _favoriteImages.value = REPOSITORY.refreshFavorites()
                Log.d("M_FavoritesViewModel", "Избранные картинки успешно загружены: ${favoriteImages.value?.size}")
            } catch (e: Exception) {
                Log.d("M_FavoritesViewModel", "Ошибка при загрузке избранных картинок: ${e.message}")
            }
        }
    }
    fun deleteFromFavorites(cat: Cat) {
        Log.d("M_FavoritesViewModel", "deleteFromFavorites запущен. Cat: ${cat.favoriteId}")
        viewModelScope.launch {
            try {
                _response.value = REPOSITORY.removeFavoriteByFavId(cat.favoriteId)
                showToast("Удалено из избранного")
                Log.d("M_FavoritesViewModel", "Удалено из избранного успешно: ${response.value}")
            } catch (e: Exception) {
                showToast("Уже удалено из избранного")
                Log.d("M_FavoritesViewModel", "Ошибка при удалении из избранного: ${e.message}")
            }
        }
    }

    fun displayCatCard(currentImage: Cat) {
        _navigateToCard.value = currentImage
    }
    fun displayCatCardComplete() {
        _navigateToCard.value = null
    }
}
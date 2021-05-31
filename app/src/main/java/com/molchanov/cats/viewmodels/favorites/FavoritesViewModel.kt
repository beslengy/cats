package com.molchanov.cats.viewmodels.favorites

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.molchanov.cats.domain.Cat
import com.molchanov.cats.utils.ApiStatus
import com.molchanov.cats.utils.REPOSITORY
import com.molchanov.cats.utils.showToast
import kotlinx.coroutines.launch

class FavoritesViewModel : ViewModel() {
    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus>
        get() = _status

    private val _favoriteImages = MutableLiveData<MutableList<Cat>>()
    val favoriteImages: LiveData<MutableList<Cat>> get() = _favoriteImages

    private val _navigateToCard = MutableLiveData<Cat>()
    val navigateToCard: LiveData<Cat>
        get() = _navigateToCard

    private val _response = MutableLiveData<String>()
    private val response: LiveData<String> get() = _response

    private val _removeItem = MutableLiveData<Cat>()
    val removeItem: LiveData<Cat>
        get() = _removeItem

    init {
        Log.d("M_FavoritesViewModel", "FavoritesViewModel инициализируется")
        getFavorites()
    }

    private fun getFavorites() {
        viewModelScope.launch {
            _status.value = ApiStatus.LOADING
            try {
                _favoriteImages.value = REPOSITORY.refreshFavorites()
                Log.d(
                    "M_FavoritesViewModel",
                    "Избранные картинки успешно загружены: ${favoriteImages.value?.size}"
                )
                _status.value = if (favoriteImages.value.isNullOrEmpty()) {
                    ApiStatus.EMPTY
                } else {
                    ApiStatus.DONE
                }
            } catch (e: Exception) {
                Log.d(
                    "M_FavoritesViewModel",
                    "Ошибка при загрузке избранных картинок: ${e.message}"
                )
                _status.value = ApiStatus.ERROR
            }
        }
        Log.d("M_FavoritesViewModel", "getFavorites отработал")
    }

    fun deleteFromFavorites(cat: Cat) {
        Log.d("M_FavoritesViewModel", "deleteFromFavorites запущен. Cat: ${cat.favoriteId}")
        viewModelScope.launch {
            try {
                _response.value = REPOSITORY.removeFavoriteByFavId(cat.favoriteId)
//                _favoriteImages.value = REPOSITORY.removeItem(cat)
                _favoriteImages.value = REPOSITORY.refreshFavorites()

                Log.d(
                    "M_FavoritesViewModel",
                    "Удалено из favorite images: ${cat.imageId}. Размер списка: ${favoriteImages.value?.size}"
                )
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
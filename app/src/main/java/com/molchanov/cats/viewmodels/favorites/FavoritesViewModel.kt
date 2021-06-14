package com.molchanov.cats.viewmodels.favorites

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.molchanov.cats.data.CatsRepository
import com.molchanov.cats.network.networkmodels.CatItem
import com.molchanov.cats.utils.ApiStatus
import com.molchanov.cats.utils.showToast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(private val repository: CatsRepository) : ViewModel() {
    private val _status = MutableLiveData<ApiStatus>(ApiStatus.DONE)
    val status: LiveData<ApiStatus>
        get() = _status



    //Переменная для хранения Paging data избранных картинок в формате live data
    val favoriteImages = repository.getFavoritesList().cachedIn(viewModelScope)
    //Переменная для перехода на карточку котика и передачи аргумента фрагменту CatCard
    val navigateToCard = MutableLiveData<CatItem>()

    private val _response = MutableLiveData<String>()
    private val response: LiveData<String> get() = _response


    fun deleteFromFavorites(cat: CatItem) {
        Log.d("M_FavoritesViewModel", "deleteFromFavorites запущен. Cat: ${cat.id}")
        try {
            viewModelScope.launch {
                _response.value = repository.removeFavoriteByFavId(cat.id)
            }
            Log.d(
                "M_FavoritesViewModel","Удалено из избранного: ${cat.image?.id}"
            )
            showToast("Удалено из избранного")
            Log.d("M_FavoritesViewModel", "Удалено из избранного успешно: ${response.value}")
        } catch (e: Exception) {
            showToast("Уже удалено из избранного")
            Log.d("M_FavoritesViewModel", "Ошибка при удалении из избранного: ${e.message}")
        }

    }

    fun displayCatCard(currentImage: CatItem) {
        navigateToCard.value = currentImage
    }

    fun displayCatCardComplete() {
        navigateToCard.value = null
    }
}
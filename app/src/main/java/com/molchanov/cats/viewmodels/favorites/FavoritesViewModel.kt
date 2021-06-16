package com.molchanov.cats.viewmodels.favorites

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.molchanov.cats.R
import com.molchanov.cats.data.CatsRepository
import com.molchanov.cats.network.networkmodels.CatItem
import com.molchanov.cats.utils.APP_ACTIVITY
import com.molchanov.cats.utils.Functions.showToast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(private val repository: CatsRepository) : ViewModel() {

    //Переменная для хранения Paging data избранных картинок в формате live data
    val favoriteImages = repository.getFavoritesList().cachedIn(viewModelScope)
    //Переменная для перехода на карточку котика и передачи аргумента фрагменту CatCard
    val navigateToCard = MutableLiveData<CatItem>()

    private val response = MutableLiveData<String>()


    fun deleteFromFavorites(cat: CatItem) {
        Log.d("M_FavoritesViewModel", "deleteFromFavorites запущен. Cat: ${cat.id}")
        try {
            viewModelScope.launch {
                response.value = repository.removeFavoriteByFavId(cat.id)
            }
            showToast(APP_ACTIVITY.resources.getString(R.string.deleted_from_favorites_toast_text))
        } catch (e: Exception) {
            showToast(APP_ACTIVITY.resources.getString(R.string.already_deleted_from_favorites_toast_text))
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
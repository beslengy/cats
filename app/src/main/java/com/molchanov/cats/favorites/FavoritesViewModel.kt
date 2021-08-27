package com.molchanov.cats.favorites

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.molchanov.cats.R
import com.molchanov.cats.data.CatsRepository
import com.molchanov.cats.network.networkmodels.CatItem
import com.molchanov.cats.utils.showToast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: CatsRepository,
    val app: Application,
) : AndroidViewModel(app) {

    private val resources = app.resources

    //Переменная для хранения Paging data избранных картинок в формате live data
    val favoriteImages = repository.getFavoritesList().cachedIn(viewModelScope)

    //Переменная для перехода на карточку котика и передачи аргумента фрагменту CatCard
    val navigateToCard = MutableLiveData<CatItem>()

    private val response = MutableLiveData<String>()


    fun deleteFromFavorites(cat: CatItem) {
        try {
            viewModelScope.launch {
                response.value = repository.removeFavoriteByFavId(cat.id)
            }
            app.showToast(
                resources.getString(R.string.deleted_from_favorites_toast_text)
            )
        } catch (e: Exception) {
            app.showToast(
                resources.getString(R.string.already_deleted_from_favorites_toast_text)
            )
        }

    }

    fun displayCatCard(currentImage: CatItem) {
        navigateToCard.value = currentImage
    }

    fun displayCatCardComplete() {
        navigateToCard.value = null
    }
}
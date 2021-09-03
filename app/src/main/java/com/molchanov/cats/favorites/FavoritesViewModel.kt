package com.molchanov.cats.favorites

import android.app.Application
import androidx.lifecycle.*
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
    private val handle: SavedStateHandle,
    private val app: Application,
) : AndroidViewModel(app) {

    private val resources = app.resources

    //Переменные для сохранения состояния прокрутки
    val rvIndex = handle.getLiveData<Int?>("rv_index", null) as LiveData<Int?>
    val rvTop = handle.getLiveData("rv_top", 0) as LiveData<Int>

    private val state = handle.getLiveData("state", true)

    //Переменная для хранения Paging data избранных картинок в формате live data
    val favoriteImages = state.switchMap { repository.getFavoritesList().cachedIn(viewModelScope) }

    //Переменная для перехода на карточку котика и передачи аргумента фрагменту CatCard
    private val _navigateToCard = MutableLiveData<CatItem>()
    val navigateToCard: LiveData<CatItem> = _navigateToCard

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
        _navigateToCard.value = currentImage
    }

    fun displayCatCardComplete() {
        _navigateToCard.value = null
    }

    fun saveScrollPosition(index: Int, top: Int) {
        handle["rv_index"] = index
        handle["rv_top"] = top
    }
}
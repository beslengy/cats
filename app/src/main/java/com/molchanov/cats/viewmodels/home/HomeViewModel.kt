package com.molchanov.cats.viewmodels.home


import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.molchanov.cats.R
import com.molchanov.cats.data.CatsRepository
import com.molchanov.cats.network.networkmodels.CatItem
import com.molchanov.cats.utils.APP_ACTIVITY
import com.molchanov.cats.utils.showToast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: CatsRepository) : ViewModel() {

    //Переменная для хранения Paging data картинок в формате live data
    val homeImages = repository.getCatList().cachedIn(viewModelScope)

    //Переменная для перехода на карточку котика и передачи аргумента фрагменту CatCard
    val navigateToCard = MutableLiveData<CatItem>()

    private val response = MutableLiveData<String>()

    fun addToFavorites(currentImage: CatItem) {
        viewModelScope.launch {
            try {
                repository.addToFavoriteByImageId(currentImage.id)
                showToast(APP_ACTIVITY.resources.getString(R.string.added_to_favorite_toast_text))
            } catch(e: Exception) {
                showToast(APP_ACTIVITY.resources.getString(R.string.already_added_to_favorite_toast_text))
                Log.d("M_HomeViewModel", "Ошибка при добавлении в избранное: ${e.message}")
            }
        }
    }
    fun displayCatCard(currentImage: CatItem) {
        navigateToCard.value = currentImage
    }

    fun displayCatCardComplete() {
        navigateToCard.value = null
    }
}
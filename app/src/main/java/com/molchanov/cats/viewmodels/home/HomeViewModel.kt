package com.molchanov.cats.viewmodels.home


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
class HomeViewModel @Inject constructor(private val repository: CatsRepository) : ViewModel() {
    private val _status = MutableLiveData<ApiStatus>(ApiStatus.DONE)
    val status: LiveData<ApiStatus>
        get() = _status


    //Переменная для хранения Paging data избранных картинок в формате live data
    val homeImages = repository.getCatList().cachedIn(viewModelScope)

    //Переменная для перехода на карточку котика и передачи аргумента фрагменту CatCard
    val navigateToCard = MutableLiveData<CatItem>()

    private val _response = MutableLiveData<String>()
    private val response: LiveData<String> get() = _response

    fun addToFavorites(currentImage: CatItem) {
        viewModelScope.launch {
            try {
                repository.addToFavoriteByImageId(currentImage.id)
                showToast("Добавлено в избранное")
            } catch(e: Exception) {
                showToast("Уже добавлено в избранное")
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
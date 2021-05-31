package com.molchanov.cats.viewmodels.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.molchanov.cats.R
import com.molchanov.cats.domain.Cat
import com.molchanov.cats.utils.APP_ACTIVITY
import com.molchanov.cats.utils.ApiStatus
import com.molchanov.cats.utils.REPOSITORY
import com.molchanov.cats.utils.showToast
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    //Переменная статуса загрузки/сети
    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus>
        get() = _status

    //В эту переменную запишем ответ с сервера
    private val _response = MutableLiveData<String>()
    private val response: LiveData<String>
        get() = _response

    private val _catImage = MutableLiveData<List<Cat>>()
    val catImage: LiveData<List<Cat>>
        get() = _catImage

    private val _navigateToCard = MutableLiveData<Cat>()
    val navigateToCard: LiveData<Cat>
        get() = _navigateToCard

    //Переменная для того, чтобы отметить картинку, как избранное
    private val _checkFavorite = MutableLiveData<Cat>()
    val checkFavorite: LiveData<Cat>
        get() = _checkFavorite


    init {
        getImages()
        Log.d("M_HomeViewModel", "метод getImages отработал")
    }

    fun getImages() {
        _status.value = ApiStatus.LOADING
        viewModelScope.launch{
            try {
                _catImage.value = REPOSITORY.refreshHome()
                Log.d("M_HomeViewModel", "картинки успешно загружены: ${catImage.value?.size}")
                _status.value = if (catImage.value.isNullOrEmpty()) {ApiStatus.EMPTY} else {ApiStatus.DONE}
            } catch (e: Exception) {
                Log.d("M_HomeViewModel", "Ошибка при загрузке картинок: ${e.message}")
                _status.value = ApiStatus.ERROR
                _catImage.value = ArrayList()
            }
        }
    }
    fun addToFavorites(currentImage: Cat) {
        Log.d("M_HomeViewModel", "addToFavorites метод запущен")
        viewModelScope.launch {
            try {
                _response.value = REPOSITORY.addToFavoriteByImageId(currentImage.imageId)
                showToast(APP_ACTIVITY.resources.getString(R.string.added_to_favorite_toast_text))
              Log.d("M_HomeViewModel", "Добавлено в избранное успешно: ${response.value}")
            } catch (e: java.lang.Exception) {
                showToast("Уже добавлено в избранное")
                Log.d("M_HomeViewModel", "Ошибка при добавлении в избранное: ${e.message}")
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
package com.molchanov.cats.viewmodels.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.molchanov.cats.R
import com.molchanov.cats.domain.Cat
import com.molchanov.cats.network.CatsApi
import com.molchanov.cats.network.PostFavorite
import com.molchanov.cats.network.ResponseFavorite
import com.molchanov.cats.utils.APP_ACTIVITY
import com.molchanov.cats.utils.REPOSITORY
import com.molchanov.cats.utils.showToast
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    //В эту переменную запишем ответ с сервера
    private val _response = MutableLiveData<ResponseFavorite>()
    val response: LiveData<ResponseFavorite>
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

    private fun getImages() {
        viewModelScope.launch{
            try {
                _catImage.value = REPOSITORY.refreshHome()
                Log.d("M_HomeViewModel", "картинки успешно загружены: ${catImage.value?.size}")
            } catch (e: Exception) {
                _catImage.value = ArrayList()
            }
        }
    }
    fun addToFavorites(currentImage: Cat) {
        Log.d("M_HomeViewModel", "addToFavorites метод запущен")
        viewModelScope.launch {
            try {
                val postFavorite = PostFavorite(currentImage.imageId)
                _response.value = CatsApi.retrofitService.postFavorite(postFavorite)
                showToast(APP_ACTIVITY.resources.getString(R.string.added_to_favorite_toast_text))
              Log.d("M_HomeViewModel", "Добавлено в избранное успешно: ${response.value?.message}")
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
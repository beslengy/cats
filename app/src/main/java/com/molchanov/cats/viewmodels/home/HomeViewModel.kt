package com.molchanov.cats.viewmodels.home

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.molchanov.cats.R
import com.molchanov.cats.network.CatsApi
import com.molchanov.cats.network.NetworkImage
import com.molchanov.cats.network.PostFavorite
import com.molchanov.cats.network.ResponseFavorite
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    //В эту переменную запишем ответ с сервера
    private val _response = MutableLiveData<ResponseFavorite>()
    val response: LiveData<ResponseFavorite>
        get() = _response

    private val _catImage = MutableLiveData<List<NetworkImage>>()
    val catImage: LiveData<List<NetworkImage>>
        get() = _catImage

    private val _navigateToCard = MutableLiveData<NetworkImage>()
    val navigateToCard: LiveData<NetworkImage>
        get() = _navigateToCard

    //Переменная для того, чтобы отметить картинку, как избранное
    private val _checkFavorite = MutableLiveData<NetworkImage>()
    val checkFavorite: LiveData<NetworkImage>
        get() = _checkFavorite

    init {
        getImages()
    }

    private fun getImages() {
        viewModelScope.launch{
            try {
                _catImage.value = CatsApi.retrofitService.getAllImages()
            } catch (e: Exception) {
                _catImage.value = ArrayList()
            }
        }
    }
    fun addToFavorites(currentImage: NetworkImage) {
        Log.d("M_HomeViewModel", "addToFavorites метод запущен")
        viewModelScope.launch {
            Log.d("M_HomeViewModel", "viewModelScope.launch отработал")
            try {
                val postFavorite = PostFavorite(currentImage.id)
                _response.value = CatsApi.retrofitService.postFavorite(postFavorite)
                Toast.makeText(getApplication<Application>().applicationContext, R.string.added_to_favorite_toast_text, Toast.LENGTH_LONG).show()
              Log.d("M_HomeViewModel", "Добавлено в избранное успешно: ${response.value?.message}")
            } catch (e: java.lang.Exception) {
                Log.d("M_HomeViewModel", "Ошибка при добавлении в избранное: ${e.message}")
            }
        }
    }

    fun displayCatCard(currentImage: NetworkImage) {
        _navigateToCard.value = currentImage
    }
    fun displayCatCardComplete() {
        _navigateToCard.value = null
    }
}
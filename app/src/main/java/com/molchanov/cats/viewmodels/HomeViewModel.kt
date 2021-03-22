package com.molchanov.cats.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.molchanov.cats.network.CatsApi
import com.molchanov.cats.network.NetworkImage
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    //В эту переменную запишем ответ с сервера
    private val _response = MutableLiveData<String>()
    val response: LiveData<String>
        get() = _response

    private val _catImage = MutableLiveData<List<NetworkImage>>()
    val catImage: LiveData<List<NetworkImage>>
        get() = _catImage

    private val _navigateToCard = MutableLiveData<NetworkImage>()
    val navigateToCard: LiveData<NetworkImage>
        get() = _navigateToCard

    init {
        getImages()
    }

    private fun getImages() {
        viewModelScope.launch{
            try {
                _catImage.value = CatsApi.retrofitService.getAllImages()
                _response.value = "Success: cat's images available"
            } catch (e: Exception) {
                _response.value = "Failure: ${e.message}"
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
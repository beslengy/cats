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

    private val _catImage = MutableLiveData<NetworkImage>()
    val catImage: LiveData<NetworkImage>
        get() = _catImage

    init {
        getCats()
    }

    private fun getCats() {
        viewModelScope.launch{
            try {
                val listResult = CatsApi.retrofitService.getImages()
                _response.value = "Success: ${listResult.size} cats available"
                if(listResult.size > 0) _catImage.value = listResult[0]
            } catch (e: Exception) {
                _response.value = "Failure: ${e.message}"
            }
        }
    }
}
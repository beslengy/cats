package com.molchanov.cats.viewmodels.uploaded

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.molchanov.cats.data.CatsRepository
import com.molchanov.cats.network.networkmodels.CatItem
import com.molchanov.cats.utils.Functions.showToast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class UploadedViewModel @Inject constructor(private val repository: CatsRepository) : ViewModel() {

    val uploadedImages = repository.getUploadedList().cachedIn(viewModelScope)


    private val response = MutableLiveData<String>()

    val navigateToCard = MutableLiveData<CatItem>()


    fun displayCatCard(currentImage: CatItem) {
        navigateToCard.value = currentImage
    }

    fun displayCatCardComplete() {
        navigateToCard.value = null
    }
    fun uploadFileByUri(uri: Uri?) {
        Log.d("M_UploadedViewModel", "uploadFileByUri запущен")
        if (uri != null) {
            viewModelScope.launch {
                try {
                    response.value = repository.uploadImage(File(uri.toString()))
                    Log.d("M_UploadedViewModel", "Картинка успешно загружена на сервер: ${response.value}")
                } catch (e: Exception) {
                    Log.d("M_UploadedViewModel", "Ошибка при загрузке изображения на сервер: ${e.message}")
                }
            }
        } else {
            showToast("uri is null")
            Log.d("M_UploadedViewModel", "uploadFileByUri: uri is null")
        }
    }
}
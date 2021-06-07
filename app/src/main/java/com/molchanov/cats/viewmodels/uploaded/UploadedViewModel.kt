package com.molchanov.cats.viewmodels.uploaded

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.molchanov.cats.network.networkmodels.CatItem
import com.molchanov.cats.utils.ApiStatus
import com.molchanov.cats.utils.REPOSITORY
import com.molchanov.cats.utils.showToast
import kotlinx.coroutines.launch
import java.io.File

class UploadedViewModel : ViewModel() {
    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus>
        get() = _status

    private val _uploadedImages = MutableLiveData<List<CatItem>>()
    val uploadedImages: LiveData<List<CatItem>>
        get() = _uploadedImages

    private val _response = MutableLiveData<String>()
    private val response: LiveData<String> get() = _response

    private val _navigateToCard = MutableLiveData<CatItem>()
    val navigateToCard: LiveData<CatItem>
        get() = _navigateToCard

    init {
        getUploaded()
    }

    private fun getUploaded() {
        viewModelScope.launch {
            _status.value = ApiStatus.LOADING
            try {
                _uploadedImages.value = REPOSITORY.refreshUploaded()
                Log.d("M_UploadedViewModel", "Uploaded картинки успешно загружены: ${uploadedImages.value?.size}")
                _status.value = if (uploadedImages.value.isNullOrEmpty()) {
                    ApiStatus.EMPTY
                } else {
                    ApiStatus.DONE
                }
            } catch (e: Exception) {
                Log.d("M_UploadedViewModel", "Ошибка при загрузке Uploaded картинок: ${e.message}")
                _status.value = ApiStatus.ERROR
            }
        }
    }

    fun displayCatCard(currentImage: CatItem) {
        _navigateToCard.value = currentImage
    }

    fun displayCatCardComplete() {
        _navigateToCard.value = null
    }
    fun uploadFileByUri(uri: Uri?) {
        Log.d("M_UploadedViewModel", "uploadFileByUri запущен")
        if (uri != null) {
            viewModelScope.launch {
                try {
                    _response.value = REPOSITORY.uploadImage(File(uri.toString()))
                    Log.d("M_UploadedViewModel", "${response.value}")
                    Log.d("M_UploadedViewModel", "Картинка успешно загружена на сервер")
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
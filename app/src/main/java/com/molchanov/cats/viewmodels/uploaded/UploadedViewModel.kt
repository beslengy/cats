package com.molchanov.cats.viewmodels.uploaded

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.molchanov.cats.data.CatsRepository
import com.molchanov.cats.network.networkmodels.CatItem
import com.molchanov.cats.utils.CURRENT_PHOTO_PATH
import com.molchanov.cats.utils.Functions.showToast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class UploadedViewModel @Inject constructor(private val repository: CatsRepository) : ViewModel() {

    val uploadedImages = repository.getUploadedList().cachedIn(viewModelScope)
    val onImageUploaded = MutableLiveData(false)

    private val response = MutableLiveData<String>()

    val navigateToCard = MutableLiveData<CatItem>()


    fun displayCatCard(currentImage: CatItem) {
        navigateToCard.value = currentImage
    }

    fun displayCatCardComplete() {
        navigateToCard.value = null
    }
    fun uploadFile(filePart: MultipartBody.Part?) {
        println(filePart)
        filePart?.let {
            viewModelScope.launch {
                try {
                    repository.uploadImage(it)
                    showToast("Изображение загружено")
                    val file = File(CURRENT_PHOTO_PATH)
                    if(CURRENT_PHOTO_PATH.isNotEmpty())
                        if (file.exists())
                            file.delete()
                    onImageUploaded.value = !(onImageUploaded.value!!)

                } catch (e: Exception) {
                    Log.d("M_UploadedViewModel", "Ошибка при загрузке фото на сервер: ${e.message}")
                    e.printStackTrace()
                }
            }
        }
    }
}
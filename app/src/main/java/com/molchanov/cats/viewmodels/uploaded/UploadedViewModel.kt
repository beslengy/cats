package com.molchanov.cats.viewmodels.uploaded

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.molchanov.cats.R
import com.molchanov.cats.data.CatsRepository
import com.molchanov.cats.network.networkmodels.Analysis
import com.molchanov.cats.network.networkmodels.CatItem
import com.molchanov.cats.utils.APP_ACTIVITY
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
    val onRefreshTrigger = MutableLiveData(false)

    private val response = MutableLiveData<String>()

    val navigateToAnalysis = MutableLiveData<Analysis>()


    fun displayAnalysis(currentImage: CatItem) {
        var analysis : Analysis?
        viewModelScope.launch {
            analysis = repository.getAnalysis(currentImage.id)
            analysis?.imageUrl = currentImage.imageUrl
            navigateToAnalysis.value = analysis
        }
    }

    fun displayAnalysisComplete() {
        navigateToAnalysis.value = null
    }

    fun uploadFile(filePart: MultipartBody.Part?) {
        showToast(APP_ACTIVITY.resources.getString(R.string.upload_start_toast_text))
        filePart?.let {
            viewModelScope.launch {
                try {
                    repository.uploadImage(it)
                    showToast(APP_ACTIVITY.resources.getString(R.string.upload_end_toast_text))
                    val file = File(CURRENT_PHOTO_PATH)
                    if (CURRENT_PHOTO_PATH.isNotEmpty())
                        if (file.exists())
                            file.delete()
                    onRefreshTrigger.value = !(onRefreshTrigger.value!!)

                } catch (e: Exception) {
                    Log.d("M_UploadedViewModel", "Ошибка при загрузке фото на сервер: ${e.message}")
                    e.printStackTrace()
                }
            }
        }
    }

    fun deleteImageFromServer(cat: CatItem) {
        viewModelScope.launch {
            try {
                repository.deleteUploadedImage(cat.id)

            } catch (e: Exception) {
                Log.d("M_UploadedViewModel",
                    "Ошибка при удалении загруженной картинки: ${e.message}")
            }
            showToast(APP_ACTIVITY.resources.getString(R.string.uploaded_image_is_deleted_toast_text))
            onRefreshTrigger.value = !(onRefreshTrigger.value!!)
        }
    }
}
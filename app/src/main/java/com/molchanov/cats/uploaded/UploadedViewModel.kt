package com.molchanov.cats.uploaded

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.molchanov.cats.R
import com.molchanov.cats.data.CatsRepository
import com.molchanov.cats.network.networkmodels.Analysis
import com.molchanov.cats.network.networkmodels.CatItem
import com.molchanov.cats.network.networkmodels.NetworkResponse
import com.molchanov.cats.utils.Global.CURRENT_PHOTO_PATH
import com.molchanov.cats.utils.showToast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class UploadedViewModel @Inject constructor(
    private val repository: CatsRepository,
    private val handle: SavedStateHandle,
    private val app: Application,
) : AndroidViewModel(app) {

    //Переменные для сохранения состояния прокрутки
    val rvIndex = handle.getLiveData<Int?>("rv_index", null) as LiveData<Int?>
    val rvTop = handle.getLiveData("rv_top", 0) as LiveData<Int>

    private val state = handle.getLiveData("state", true)

    val uploadedImages = state.switchMap { repository.getUploadedList().cachedIn(viewModelScope) }

    private val resources = app.resources

    private val _onRefreshTrigger = MutableLiveData<Boolean?>(null)
    val onRefreshTrigger: LiveData<Boolean?> get() = _onRefreshTrigger

    private val response = MutableLiveData<NetworkResponse>()

    private val _navigateToAnalysis = MutableLiveData<Analysis>()
    val navigateToAnalysis: LiveData<Analysis> get() = _navigateToAnalysis


    fun displayAnalysis(currentImage: CatItem) {
        var analysis: Analysis?
        viewModelScope.launch {
            analysis = repository.getAnalysis(currentImage.id)
            analysis?.imageUrl = currentImage.imageUrl
            _navigateToAnalysis.value = analysis
        }
    }

    fun displayAnalysisComplete() {
        _navigateToAnalysis.value = null
    }

    fun uploadFile(filePart: MultipartBody.Part?) {
        app.showToast(resources.getString(R.string.upload_start_toast_text))
        filePart?.let {
            viewModelScope.launch {
                try {
                    response.value = repository.uploadImage(it)
                    val file = File(CURRENT_PHOTO_PATH)
                    if (CURRENT_PHOTO_PATH.isNotEmpty())
                        if (file.exists())
                            file.delete()
                    _onRefreshTrigger.value = !(_onRefreshTrigger.value!!)
                    app.showToast(
                        resources.getString(R.string.upload_end_toast_text)
                    )
                } catch (e: Exception) {
                    app.showToast(
                        resources.getString(R.string.upload_fail_toast_text)
                    )
                }
            }
        }
    }

    fun deleteImageFromServer(cat: CatItem) {
        viewModelScope.launch {
            repository.deleteUploadedImage(cat.id)
            app.showToast(
                resources.getString(R.string.uploaded_image_is_deleted_toast_text)
            )
            _onRefreshTrigger.value = true
        }
    }

    fun saveScrollPosition(index: Int, top: Int) {
        Log.d("M_HomeViewModel", "saveScroll: $index")
        handle["rv_index"] = index
        handle["rv_top"] = top
    }

    fun refreshComplete() {
        _onRefreshTrigger.value = null
    }
}
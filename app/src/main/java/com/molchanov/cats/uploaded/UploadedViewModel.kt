package com.molchanov.cats.uploaded

import android.app.Application
import android.net.Uri
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
    val uploadedImages = repository.getUploadedList().cachedIn(viewModelScope)
    private val filenames = MutableLiveData<List<CatItem>>()
    private val resources = app.resources

    private val _response = MutableLiveData<NetworkResponse>()
    val response: LiveData<NetworkResponse> = _response

    private val _navigateToAnalysis = MutableLiveData<Analysis>()
    val navigateToAnalysis: LiveData<Analysis> get() = _navigateToAnalysis

    private val _isFileExist = MutableLiveData<Boolean>()
    val isFileExist: LiveData<Boolean> = _isFileExist

    fun displayAnalysis(currentImage: CatItem) {
        var analysis: Analysis?
        viewModelScope.launch {
            analysis = repository.getAnalysis(currentImage.id)
            analysis?.imageUrl = currentImage.imageUrl
            _navigateToAnalysis.apply {
                value = analysis
                value = null
            }
        }
    }

    fun uploadFile(filePart: MultipartBody.Part?) {
        app.showToast(resources.getString(R.string.upload_start_toast_text))
        filePart?.let {
            viewModelScope.launch {
                try {
                    _response.value = repository.uploadImage(it)
                    val file = File(CURRENT_PHOTO_PATH)
                    if (CURRENT_PHOTO_PATH.isNotEmpty())
                        if (file.exists())
                            file.delete()
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
            try {
                repository.deleteUploadedImage(cat.id)
            } catch (e: Exception) {
            }
            app.showToast(
                resources.getString(R.string.uploaded_image_is_deleted_toast_text)
            )
        }
    }

    fun saveScrollPosition(index: Int, top: Int) {
        handle["rv_index"] = index
        handle["rv_top"] = top
    }

    fun checkFileIsExist(uri: Uri) {
        val filename = File(uri.path!!).name
        if (filenames.value!!.isEmpty()) {
            _isFileExist.value = false
        } else {
            for (item in filenames.value!!) {
                if (item.filename == filename) {
                    _isFileExist.value = true
                    break
                } else {
                    _isFileExist.value = false
                }
            }
        }
    }

    fun getFilenames() {
        viewModelScope.launch {
            filenames.value = repository.getFilenames()
        }
    }

    fun fileExistCheckingComplete() {
        _isFileExist.value = null
    }
}
package com.molchanov.cats.viewmodels.catcard

import android.util.Log
import androidx.lifecycle.*
import com.molchanov.cats.data.CatsRepository
import com.molchanov.cats.network.networkmodels.CatDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CatCardViewModel @Inject constructor(
    private val repository: CatsRepository,
    handle: SavedStateHandle
) : ViewModel() {
    private val _cat = MutableLiveData<CatDetail>()
    val cat: LiveData<CatDetail> get() = _cat
    val imageId: String = handle.get("imageId")!!

    init {
        Log.d("M_CatCardViewModel", imageId)
        getCat()

    }

    private fun getCat() {
        Log.d("M_CatCardViewModel", "getCat запущен")
        viewModelScope.launch {
            try {
                _cat.value = repository.getCatById(imageId)
                Log.d("M_CatCardViewModel", "Картинка успешно загружена")
            } catch (e: Exception) {
                Log.d("M_CatCardViewModel", "Ошибка при загрузке карточки: ${e.message}")
            }
        }
    }
}
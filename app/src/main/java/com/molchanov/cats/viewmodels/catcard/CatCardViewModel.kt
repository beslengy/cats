package com.molchanov.cats.viewmodels.catcard

import android.util.Log
import androidx.lifecycle.*
import com.molchanov.cats.network.networkmodels.CatDetail
import com.molchanov.cats.repository.CatsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

class CatCardViewModel (
     private val repository: CatsRepository,
     private val imageId: String) : ViewModel(), LifecycleObserver {
     private val _cat = MutableLiveData<CatDetail>()
     val cat: LiveData<CatDetail> get() = _cat

     init {
         Log.d("M_CatCardViewModel", imageId)
          getCat()
          Log.d("M_CatCardViewModel", "getCat запущен")
     }

     private fun getCat() {
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
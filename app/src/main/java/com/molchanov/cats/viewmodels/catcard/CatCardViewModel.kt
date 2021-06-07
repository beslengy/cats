package com.molchanov.cats.viewmodels.catcard

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.molchanov.cats.network.networkmodels.CatDetail
import com.molchanov.cats.utils.REPOSITORY
import kotlinx.coroutines.launch

class CatCardViewModel(private val imageId: String, app: Application) : AndroidViewModel(app) {
     private val _cat = MutableLiveData<CatDetail>()
     val cat: LiveData<CatDetail> get() = _cat

     init {
          getCat()
          Log.d("M_CatCardViewModel", "getCat запущен")
     }

     private fun getCat() {
          viewModelScope.launch {
               try {
                   _cat.value = REPOSITORY.getCatById(imageId)
                    Log.d("M_CatCardViewModel", "Картинка успешно загружена")
               } catch (e: Exception) {
                    Log.d("M_CatCardViewModel", "Ошибка при загрузке карточки: ${e.message}")
               }
          }
     }
}
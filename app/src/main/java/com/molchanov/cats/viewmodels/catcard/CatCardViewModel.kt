package com.molchanov.cats.viewmodels.catcard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.molchanov.cats.network.CatsApi
import com.molchanov.cats.network.NetworkCat
import kotlinx.coroutines.launch

class CatCardViewModel(private val imageId: String, app: Application) : AndroidViewModel(app) {
     private val _cat = MutableLiveData<NetworkCat>()
     val cat: LiveData<NetworkCat> get() = _cat

     init {
          getCat()
     }

     private fun getCat() {
          viewModelScope.launch {
               try {
                   _cat.value = CatsApi.retrofitService.getCatByImage(imageId)
               } catch (e: Exception) {

               }
          }
     }
}
package com.molchanov.cats.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.molchanov.cats.network.CatsApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {
    //В эту переменную запишем ответ с сервера
    private val _response = MutableLiveData<String>()
    val response: LiveData<String>
        get() = _response

    init {
        getCats()
    }

    private fun getCats() {
        CatsApi.retrofitService.getCats().enqueue(
            object: Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    _response.value = response.body()
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    _response.value = "Failure: ${t.message}"
                }
            }
        )
    }
}
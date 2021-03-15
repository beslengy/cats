package com.molchanov.cats.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.molchanov.cats.network.CatsApi
import com.molchanov.cats.network.NetworkCats
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
            object: Callback<List<NetworkCats>> {
                override fun onResponse(call: Call<List<NetworkCats>>, response: Response<List<NetworkCats>>) {
                    _response.value = "${response.body()?.size}"
                }

                override fun onFailure(call: Call<List<NetworkCats>>, t: Throwable) {
                    _response.value = "Failure: ${t.message}"
                }
            }
        )
    }
}
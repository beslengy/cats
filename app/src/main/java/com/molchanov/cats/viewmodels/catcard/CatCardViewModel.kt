package com.molchanov.cats.viewmodels.catcard

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.molchanov.cats.data.CatsRepository
import com.molchanov.cats.network.networkmodels.Analysis
import com.molchanov.cats.network.networkmodels.CatDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CatCardViewModel @Inject constructor(
    private val repository: CatsRepository,
    handle: SavedStateHandle,
) : ViewModel() {

    companion object {
        private const val VOTE_UP_VALUE = 1
        private const val VOTE_DOWN_VALUE = 0
        private const val NOT_VOTED_VALUE = -1
    }

    val cat = MutableLiveData<CatDetail>()
    val analysis = MutableLiveData<Analysis?>(handle.get("analysis"))

    private val imageId: String? = handle.get("imageId")
    val voteValue: MutableLiveData<Int> = handle.getLiveData("voteValue")

    private var voteId: String = ""

    private var response: String = ""

    init {
        Log.d("M_CatCardViewModel", "init. Analysis = ${analysis.value}")
        if (analysis.value == null) {
            getCat()
        }
    }

    private fun getCat() {
        imageId?.let {
            Log.d("M_CatCardViewModel", "getCat запущен")
            viewModelScope.launch {
                try {
                    cat.value = repository.getCatById(it)
                    Log.d("M_CatCardViewModel", "Картинка успешно загружена")
                } catch (e: Exception) {
                    Log.d("M_CatCardViewModel", "Ошибка при загрузке карточки: ${e.message}")
                }
            }
        }

    }

    fun voteUp() {
        imageId?.let {
            viewModelScope.launch {
                try {
                    val responseBody = repository.postVote(imageId, VOTE_UP_VALUE)
                    response = responseBody.message
                    voteId = responseBody.id
                    voteValue.value = VOTE_UP_VALUE
                    Log.d("M_CatCardViewModel",
                        "Голос отправлен успешно. $response. VoteId = $voteId")
                } catch (e: Exception) {
                    Log.d("M_CatCardViewModel", "Ошибка при отправке голоса. ${e.message}")
                }
            }
        }
    }

    fun voteDown() {
        imageId?.let {
            viewModelScope.launch {
                try {
                    val responseBody = repository.postVote(imageId, VOTE_DOWN_VALUE)
                    response = responseBody.message
                    voteId = responseBody.id
                    voteValue.value = VOTE_DOWN_VALUE
                    Log.d("M_CatCardViewModel", "Голос отправлен успешно: $response")
                } catch (e: Exception) {
                    Log.d("M_CatCardViewModel", "Ошибка при отправке голоса: ${e.message}")
                }
            }
        }
    }

    fun removeVote() {
        viewModelScope.launch {
            try {
                response = repository.deleteVote(voteId)
                voteValue.value = NOT_VOTED_VALUE
                Log.d("M_CatCardViewModel", "Голос успешно удален: $response")
            } catch (e: Exception) {
                Log.d("M_CatCardViewModel", "Ошибка при удалении голоса: ${e.message}")
            }
        }
    }
}

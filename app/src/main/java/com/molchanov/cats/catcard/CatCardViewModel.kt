package com.molchanov.cats.catcard

import androidx.lifecycle.*
import com.molchanov.cats.data.CatsRepository
import com.molchanov.cats.network.networkmodels.Analysis
import com.molchanov.cats.network.networkmodels.CatDetail
import com.molchanov.cats.network.networkmodels.Vote
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

    private val _cat = MutableLiveData<CatDetail>()
    val cat: LiveData<CatDetail> get() = _cat

    private val _analysis = MutableLiveData<Analysis?>(handle.get("analysis"))
    val analysis: LiveData<Analysis?> get() = _analysis

    private val votes = MutableLiveData<List<Vote>>()

    private val imageId: String? = handle.get("imageId")

    private val _voteValue = MutableLiveData(NOT_VOTED_VALUE)
    val voteValue: LiveData<Int> get() = _voteValue

    private var voteId: String = ""

    private var response: String = ""

    init {
        if (_analysis.value == null) {
            getCat()
            setVoteValue()
        }
    }

    private fun setVoteValue() {
        viewModelScope.launch {
            votes.value = repository.getVotes()
            votes.value?.let { votes ->
                for (v in votes) {
                    if (v.imageId == imageId) {
                        _voteValue.value = v.value
                        voteId = v.voteId
                        break
                    } else {
                        _voteValue.value = NOT_VOTED_VALUE
                    }
                }
            }
        }
    }

    private fun getCat() {
        imageId?.let {
            viewModelScope.launch {
                try {
                    _cat.value = repository.getCatById(it)
                } catch (e: Exception) {
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
                    _voteValue.value = VOTE_UP_VALUE
                } catch (e: Exception) {
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
                    _voteValue.value = VOTE_DOWN_VALUE
                } catch (e: Exception) {
                }
            }
        }
    }

    fun removeVote() {
        viewModelScope.launch {
            try {
                response = repository.deleteVote(voteId)
                _voteValue.value = NOT_VOTED_VALUE
            } catch (e: Exception) {
            }
        }
    }
}

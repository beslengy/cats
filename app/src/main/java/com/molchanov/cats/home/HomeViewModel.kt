package com.molchanov.cats.home


import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.molchanov.cats.data.CatsRepository
import com.molchanov.cats.network.networkmodels.CatItem
import com.molchanov.cats.network.networkmodels.FilterItem
import com.molchanov.cats.utils.BREEDS_FILTER_TYPE
import com.molchanov.cats.utils.CATEGORIES_FILTER_TYPE
import com.molchanov.cats.utils.DEFAULT_FILTER_TYPE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: CatsRepository,
    handle: SavedStateHandle,
    application: Application,
) : AndroidViewModel(application) {
    private val currentQuery = handle.getLiveData(CURRENT_QUERY, DEFAULT_QUERY)

    // Переменная хранит тип тоста и при изменении вызвает тост с нужным текстом
    private val _toast = MutableLiveData<ToastRequest>(null)
    val toast: LiveData<ToastRequest> get() = _toast

    //Переменная для хранения Paging data картинок в формате live data
    val homeImages = currentQuery.switchMap { repository.getCatList(it).cachedIn(viewModelScope) }


    //Переменная для перехода на карточку котика и передачи аргумента фрагменту CatCard
    private val _navigateToCard = MutableLiveData<CatItem>()
    val navigateToCard: LiveData<CatItem> get() = _navigateToCard


    val currentFilterType = MutableLiveData(DEFAULT_FILTER_TYPE)
    val currentFilterItem = MutableLiveData<FilterItem>(null)

    private val _breeds = MutableLiveData<List<FilterItem>>()
    val breeds: LiveData<List<FilterItem>> get() = _breeds

    private val _categories = MutableLiveData<List<FilterItem>>()
    val categories: LiveData<List<FilterItem>> get() = _categories

    init {
        getCategories()
        getBreeds()
    }

    fun addToFavorites(currentImage: CatItem) {
        Log.d("M_HomeViewModel", "addToFav")
        viewModelScope.launch {
            try {
                val favId = repository.addToFavoriteByImageId(currentImage.id)
                currentImage.apply {
                    favourite = CatItem.Favourite(favId)
                    isFavorite = true
                }
                _toast.value = ToastRequest.ADD_FAV
            } catch (e: Exception) {
                _toast.value = ToastRequest.ADD_FAV_FAIL
            }
        }
    }

    fun deleteFromFavorites(cat: CatItem) {
        Log.d("M_HomeViewModel", "deleteFromFav")
        viewModelScope.launch {
            try {
                cat.apply {
                    favourite?.let {
                        repository.removeFavoriteByFavId(it.favId)
                        favourite = null
                        isFavorite = false
                    }
                    _toast.value = ToastRequest.DELETE_FAV
                }
            } catch (e: Exception) {
                _toast.value = ToastRequest.DELETE_FAV_FAIL
            }
            _toast.value = null
        }
    }

    fun displayCatCard(currentImage: CatItem) {
        _navigateToCard.value = currentImage
    }

    fun displayCatCardComplete() {
        _navigateToCard.value = null
    }

    fun toastShowComplete() {
        _toast.value = null
    }

    private fun getBreeds() {
        viewModelScope.launch {
            _breeds.value = repository.getBreedsArray()
        }
    }

    private fun getCategories() {
        viewModelScope.launch {
            _categories.value = repository.getCategoriesArray()
        }
    }

    fun setFilterType(type: String?) {
        currentFilterType.value = type
        currentFilterItem.value = null
    }

    fun setFilterItem(item: FilterItem) {
        currentFilterItem.value = item
    }

    fun setQuery() {
        currentQuery.value = if (currentFilterItem.value != null) {
            when (currentFilterType.value) {
                BREEDS_FILTER_TYPE -> mapOf(
                    "breed_ids" to currentFilterItem.value!!.id,
                    "category_ids" to "",
                    "order" to "DESC"
                )
                CATEGORIES_FILTER_TYPE -> mapOf(
                    "breed_ids" to "",
                    "category_ids" to currentFilterItem.value!!.id,
                    "order" to "DESC"
                )
                else -> DEFAULT_QUERY
            }
        } else {
            DEFAULT_QUERY
        }
        Log.d("M_HomeViewModel", "query = ${currentQuery.value}")
    }

    companion object {
        private const val CURRENT_QUERY = "current_query"
        private val DEFAULT_QUERY = mapOf(
            "breed_ids" to "",
            "category_ids" to "",
            "order" to "RANDOM")

        enum class ToastRequest {
            ADD_FAV,
            ADD_FAV_FAIL,
            DELETE_FAV,
            DELETE_FAV_FAIL
        }
    }
}
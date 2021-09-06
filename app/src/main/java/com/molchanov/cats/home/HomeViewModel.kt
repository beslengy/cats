package com.molchanov.cats.home


import android.app.Application
import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.molchanov.cats.data.CatsRepository
import com.molchanov.cats.network.networkmodels.CatItem
import com.molchanov.cats.network.networkmodels.FilterItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: CatsRepository,
    private val handle: SavedStateHandle,
    app: Application,
) : AndroidViewModel(app) {

    private val currentQuery = handle.getLiveData(CURRENT_QUERY, DEFAULT_QUERY)

    //Переменные для сохранения состояния прокрутки
    val rvIndex = handle.getLiveData<Int?>("rv_index", null) as LiveData<Int?>
    val rvTop = handle.getLiveData("rv_top", 0) as LiveData<Int>


    // Переменная хранит тип тоста и при изменении вызвает тост с нужным текстом
    private val _toast = MutableLiveData<ToastRequest>(null)
    val toast: LiveData<ToastRequest> get() = _toast

    //Переменная для хранения Paging data картинок в формате live data
    val homeImages = currentQuery.switchMap { repository.getCatList(it).cachedIn(viewModelScope) }

    //Переменная для перехода на карточку котика и передачи аргумента фрагменту CatCard
    private val _navigateToCard = MutableLiveData<CatItem>()
    val navigateToCard: LiveData<CatItem> get() = _navigateToCard

    private val _currentFilterType = MutableLiveData(DEFAULT_FILTER_TYPE)
    val currentFilterType: LiveData<String> get() = _currentFilterType

    private val _currentFilterItem = MutableLiveData<FilterItem>(null)
    val currentFilterItem: LiveData<FilterItem> get() = _currentFilterItem

    private val _breeds = MutableLiveData<List<FilterItem>>()
    val breeds: LiveData<List<FilterItem>> get() = _breeds

    private val _categories = MutableLiveData<List<FilterItem>>()
    val categories: LiveData<List<FilterItem>> get() = _categories

    init {
        getCategories()
        getBreeds()
    }

    fun addToFavorites(currentImage: CatItem) {
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
            _toast.value = null
        }
    }

    fun deleteFromFavorites(cat: CatItem) {
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
        _navigateToCard.apply {
            value = currentImage
            value = null
        }
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
        _currentFilterType.value = type
        _currentFilterItem.value = null
    }

    fun setFilterItem(item: FilterItem) {
        _currentFilterItem.value = item
    }

    fun setQuery() {
        currentQuery.value = if (_currentFilterItem.value != null) {
            when (_currentFilterType.value) {
                BREEDS_FILTER_TYPE -> mapOf(
                    "breed_ids" to _currentFilterItem.value!!.id,
                    "category_ids" to "",
                    "order" to "DESC"
                )
                CATEGORIES_FILTER_TYPE -> mapOf(
                    "breed_ids" to "",
                    "category_ids" to _currentFilterItem.value!!.id,
                    "order" to "DESC"
                )
                else -> DEFAULT_QUERY
            }
        } else {
            DEFAULT_QUERY
        }
    }

    companion object {
        const val DEFAULT_FILTER_TYPE = "Любой"
        const val BREEDS_FILTER_TYPE = "Порода"
        const val CATEGORIES_FILTER_TYPE = "Категория"
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

    fun saveScrollPosition(index: Int, top: Int) {
        handle["rv_index"] = index
        handle["rv_top"] = top
    }
}
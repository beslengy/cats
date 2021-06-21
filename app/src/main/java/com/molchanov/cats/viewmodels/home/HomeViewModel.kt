package com.molchanov.cats.viewmodels.home


import android.util.Log
import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.molchanov.cats.R
import com.molchanov.cats.data.CatsRepository
import com.molchanov.cats.network.networkmodels.CatItem
import com.molchanov.cats.network.networkmodels.FilterItem
import com.molchanov.cats.utils.APP_ACTIVITY
import com.molchanov.cats.utils.BREEDS_FILTER_TYPE
import com.molchanov.cats.utils.CATEGORIES_FILTER_TYPE
import com.molchanov.cats.utils.DEFAULT_FILTER_TYPE
import com.molchanov.cats.utils.Functions.showToast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: CatsRepository,
    handle: SavedStateHandle,
) : ViewModel() {
    private val currentQuery = handle.getLiveData(CURRENT_QUERY, DEFAULT_QUERY)

    //Переменная для хранения Paging data картинок в формате live data
    val homeImages = currentQuery.switchMap { repository.getCatList(it).cachedIn(viewModelScope) }

    //Переменная для перехода на карточку котика и передачи аргумента фрагменту CatCard
    val navigateToCard = MutableLiveData<CatItem>()


    private val response = MutableLiveData<String>()

    val currentFilterType = MutableLiveData(DEFAULT_FILTER_TYPE)
    val currentFilterItem = MutableLiveData<FilterItem>(null)

    val breeds = MutableLiveData<List<FilterItem>>()
    val categories = MutableLiveData<List<FilterItem>>()

    init {
        getCategories()
        getBreeds()
    }

    fun addToFavorites(currentImage: CatItem) {
        viewModelScope.launch {
            try {
                repository.addToFavoriteByImageId(currentImage.id)
                showToast(APP_ACTIVITY.resources.getString(R.string.added_to_favorite_toast_text))
            } catch (e: Exception) {
                showToast(APP_ACTIVITY.resources.getString(R.string.already_added_to_favorite_toast_text))
                Log.d("M_HomeViewModel", "Ошибка при добавлении в избранное: ${e.message}")
            }
        }
    }

    fun displayCatCard(currentImage: CatItem) {
        navigateToCard.value = currentImage
    }

    fun displayCatCardComplete() {
        navigateToCard.value = null
    }

    private fun getBreeds() {
        Log.d("M_HomeViewModel", "getBreeds запущен")
        viewModelScope.launch {
            breeds.value = repository.getBreedsArray()
            Log.d("M_HomeViewModel", "filter items = ${breeds.value}")
        }
    }

    private fun getCategories() {
        Log.d("M_HomeViewModel", "getCategories запущен")
        categories.value = runBlocking { repository.getCategoriesArray() }
        Log.d("M_HomeViewModel", "filter items = ${categories.value}")
    }

    fun setFilterType(type: String?) {
        currentFilterType.value = type
        currentFilterItem.value = null
    }

    fun setFilterItem(item: FilterItem) {
        currentFilterItem.value = item
        Log.d("M_HomeViewModel", "currentFilterItemValue = ${currentFilterItem.value}")
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
    }
}
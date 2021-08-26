package com.molchanov.cats.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.molchanov.cats.favorites.FavoritePagingSource
import com.molchanov.cats.home.HomePagingSource
import com.molchanov.cats.network.CatsApiService
import com.molchanov.cats.network.networkmodels.*
import com.molchanov.cats.uploaded.UploadedPagingSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CatsRepository @Inject constructor(private val catsApi: CatsApiService) {
    var votes: List<Vote> = listOf()
    private lateinit var cat: CatDetail
    private var analysis: Analysis? = null

    fun getCatList(query: Map<String, String>): LiveData<PagingData<CatItem>> {
        Log.d("M_CatsRepository", "getCatList запущен")
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,
                maxSize = 100,
                initialLoadSize = 20
            ),
            pagingSourceFactory = { HomePagingSource(catsApi, query) }
        ).liveData
    }

    fun getFavoritesList(): LiveData<PagingData<CatItem>> {
        Log.d("M_CatsRepository", "getFavoritesList запущен")

        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,
                maxSize = 100,
                initialLoadSize = 20
            ),
            pagingSourceFactory = { FavoritePagingSource(catsApi) }
        ).liveData
    }

    fun getUploadedList() =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,
                maxSize = 100,
                initialLoadSize = 20

            ),
            pagingSourceFactory = { UploadedPagingSource(catsApi) }
        ).liveData

    suspend fun addToFavoriteByImageId(imageId: String): String {
        val favId: String
        val postFavorite = PostFavorite(imageId)
        withContext(Dispatchers.IO) {
            favId = catsApi.postFavorite(postFavorite).id
            Log.d("M_CatsRepository", "fav id = $favId")
        }
        return favId
    }

    suspend fun getCatById(imageId: String): CatDetail {
        withContext(Dispatchers.IO) {
            try {
                cat = catsApi.getCatByImage(imageId)
                Log.d("M_CatsRepository", "$cat")
            } catch (e: IOException) {
                Log.d("M_CatsRepository", "Ошибка при загрузке карточки котика: ${e.message}")
            }
        }
        return cat
    }

    suspend fun removeFavoriteByFavId(favId: String): String {
        Log.d("M_CatsRepository", "removeFavoritesById запущен, favId: $favId")
        val message: String
        withContext(Dispatchers.IO) {
            message = catsApi.deleteFavorite(favId).message
        }
        return message
    }


    suspend fun uploadImage(file: MultipartBody.Part) : NetworkResponse {
        Log.d("M_CatsRepository", "uploadImage запущен")
        val response: NetworkResponse
        withContext(Dispatchers.IO) {
                response = catsApi.uploadImage(file)
        }
        return response
    }

    suspend fun getBreedsArray(): List<FilterItem> = withContext(Dispatchers.IO) { catsApi.getBreeds() }

    suspend fun getCategoriesArray(): List<FilterItem> = withContext(Dispatchers.IO) { catsApi.getCategories() }

    suspend fun postVote(imageId: String, voteValue: Int): NetworkResponse {
        val response: NetworkResponse
        val postBody = PostVote(imageId, voteValue)
        withContext(Dispatchers.IO) { response = catsApi.postVote(postBody) }
        return response
    }

    suspend fun deleteVote(voteId: String) : String {
        val message: String
        withContext(Dispatchers.IO) { message = catsApi.deleteVote(voteId).message }
        return message
    }

    suspend fun deleteUploadedImage(imageId: String) {
        withContext(Dispatchers.IO) { catsApi.deleteUploaded(imageId) }
    }

    suspend fun getAnalysis(imageId: String) : Analysis? {
        withContext(Dispatchers.IO) {
            try {
                analysis = catsApi.getAnalysis(imageId)[0]
            } catch (e: IOException) {
                Log.d("M_CatsRepository", "Ошибка при получении анализа картинки: ${e.message}")
            }
        }
        return analysis
    }

    suspend fun getVotes() : List<Vote> {
        withContext(Dispatchers.IO) {
            try {
                votes = catsApi.getAllVotes()
            } catch (e: IOException) {
                Log.d("M_CatsRepository", "Ошибка при получении списка всех голосов")
            }
        }
        return votes
    }
}

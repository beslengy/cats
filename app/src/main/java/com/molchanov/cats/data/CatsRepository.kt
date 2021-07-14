package com.molchanov.cats.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.molchanov.cats.network.CatsApiService
import com.molchanov.cats.network.networkmodels.*
import com.molchanov.cats.utils.USER_ID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CatsRepository @Inject constructor(private val catsApi: CatsApiService) {
    var cats: List<CatItem> = listOf()
    private lateinit var cat: CatDetail

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


//    suspend fun refreshHome(): List<CatItem> {
//        withContext(Dispatchers.IO) {
//            cats = catsApi.getAllImages()
//        }
//        return cats
//    }

    suspend fun addToFavoriteByImageId(imageId: String): String {
        val message: String
        val postFavorite = PostFavorite(imageId)
        withContext(Dispatchers.IO) {
            message = catsApi.postFavorite(postFavorite).message
        }
        return message
    }

//    suspend fun refreshFavorites(): List<CatItem> {
//        withContext(Dispatchers.IO) {
//            cats = catsApi.getAllFavorites(FAV_QUERY_OPTIONS)
//        }
//        Log.d("M_CatsRepository", "Избранные картинки обновлены")
//        return cats
//    }

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


    suspend fun uploadImage(file: File): String {
        Log.d("M_CatsRepository", "uploadImage запущен")
        var message: String
        val fileRequest = file.asRequestBody(("image/".toMediaTypeOrNull()))
        val usernameRequest = USER_ID.toRequestBody(("text/plain".toMediaTypeOrNull()))

        withContext(Dispatchers.IO) {
            try {
                message = catsApi.uploadImage(fileRequest, usernameRequest).message
                Log.d("M_CatsRepository", message)
            } catch (e: IOException) {
                message = e.message.toString()
                Log.d("M_CatsRepository", "Ошибка при загрузке изображения на сервер: ${e.message}")
            }
        }
        return message
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

}

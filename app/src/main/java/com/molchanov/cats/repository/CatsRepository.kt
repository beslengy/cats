package com.molchanov.cats.repository

import android.util.Log
import com.molchanov.cats.network.CatsApi
import com.molchanov.cats.network.networkmodels.CatDetail
import com.molchanov.cats.network.networkmodels.CatItem
import com.molchanov.cats.network.networkmodels.PostFavorite
import com.molchanov.cats.utils.FAV_QUERY_OPTIONS
import com.molchanov.cats.utils.USER_ID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.File
import java.io.IOException

class CatsRepository {
    var cats: List<CatItem> = listOf()
    lateinit var cat: CatDetail

    suspend fun refreshHome(): List<CatItem> {
        withContext(Dispatchers.IO) {
            cats = CatsApi.retrofitService.getAllImages()
        }
        return cats
    }
    suspend fun addToFavoriteByImageId(imageId: String): String {
        val message: String
        val postFavorite = PostFavorite(imageId)
        withContext(Dispatchers.IO){
            message = CatsApi.retrofitService.postFavorite(postFavorite).message
        }
        return message
    }

    suspend fun refreshFavorites(): List<CatItem> {
        withContext(Dispatchers.IO) {
            cats = CatsApi.retrofitService.getAllFavorites(FAV_QUERY_OPTIONS)
        }
        Log.d("M_CatsRepository", "Избранные картинки обновлены")
        return cats
    }

    suspend fun getCatById(imageId: String) : CatDetail {
        withContext(Dispatchers.IO) {
            try{
                cat = CatsApi.retrofitService.getCatByImage(imageId)
                Log.d("M_CatsRepository", "$cat")
            } catch (e: IOException) {
                Log.d("M_CatsRepository", "Ошибка при загрузке карточки котика: ${e.message}")
            }
        }
        return cat
    }

    suspend fun removeFavoriteByFavId(favId: String) : String {
        Log.d("M_CatsRepository", "removeFavoritesById запущен, favId: $favId")
        val message: String
        withContext(Dispatchers.IO) {
            message = CatsApi.retrofitService.deleteFavorite(favId).message
        }
        return message
    }


    suspend fun refreshUploaded() : List<CatItem> {
        withContext(Dispatchers.IO) {
            cats = CatsApi.retrofitService.getAllUploaded(FAV_QUERY_OPTIONS)
        }
        return cats
    }
    suspend fun uploadImage(file: File) : String {
        Log.d("M_CatsRepository", "uploadImage запущен")
        var message: String
        val fileRequest = RequestBody.create((MediaType.parse("image/")), file)
        val usernameRequest = RequestBody.create((MediaType.parse("text/plain")), USER_ID)

        withContext(Dispatchers.IO) {
            try{
                message = CatsApi.retrofitService.uploadImage(fileRequest, usernameRequest).message
                Log.d("M_CatsRepository", message)
            } catch (e: IOException) {
                message = e.message.toString()
                Log.d("M_CatsRepository", "Ошибка при загрузке изображения на сервер: ${e.message}")
            }
        }
        return message
    }
}
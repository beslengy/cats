package com.molchanov.cats.repository

import android.util.Log
import com.molchanov.cats.domain.Cat
import com.molchanov.cats.network.CatsApi
import com.molchanov.cats.network.networkmodels.PostFavorite
import com.molchanov.cats.utils.FAV_QUERY_OPTIONS
import com.molchanov.cats.utils.USER_ID
import com.molchanov.cats.utils.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.File
import java.io.IOException

class CatsRepository {
//    var cats: List<Cat> = listOf()
    var cats: MutableList<Cat> = mutableListOf()
    lateinit var cat: Cat

    suspend fun refreshHome(): MutableList<Cat> {
        withContext(Dispatchers.IO) {
            cats = CatsApi.retrofitService.getAllImages().asDomainModel() as MutableList<Cat>
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

    suspend fun refreshFavorites(): MutableList<Cat> {
        withContext(Dispatchers.IO) {
            cats = CatsApi.retrofitService.getAllFavorites(FAV_QUERY_OPTIONS).map{
                it.asDomainModel()
            } as MutableList<Cat>
        }
        Log.d("M_CatsRepository", "Избранные картинки обновлены")
        return cats
    }

    suspend fun getCatById(imageId: String) : Cat {
        withContext(Dispatchers.IO) {
            try{
                cat = CatsApi.retrofitService.getCatByImage(imageId).asDomainModel()
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

    fun removeItem(cat: Cat) : MutableList<Cat> {
        cats.remove(cat)
        return cats
    }

    suspend fun refreshUploaded() : MutableList<Cat> {
        withContext(Dispatchers.IO) {
            cats = CatsApi.retrofitService.getAllUploaded(FAV_QUERY_OPTIONS).map{
                it.asDomainModel()
            } as MutableList<Cat>
        }
        return cats
    }
    suspend fun uploadImage(file: File) : String {
        Log.d("M_CatsRepository", "uploadImage запущен")
        var message: String
        val fileRequest = RequestBody.create(MediaType.parse("image/"), file)
        val usernameRequest = RequestBody.create(MediaType.parse("text/plain"), USER_ID)

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
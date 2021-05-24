package com.molchanov.cats.repository

import android.util.Log
import com.molchanov.cats.domain.Cat
import com.molchanov.cats.network.CatsApi
import com.molchanov.cats.network.PostFavorite
import com.molchanov.cats.utils.FAV_QUERY_OPTIONS
import com.molchanov.cats.utils.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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
            cat = CatsApi.retrofitService.getCatByImage(imageId).asDomainModel()
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
    suspend fun removeItem(cat: Cat) : MutableList<Cat> {
        cats.remove(cat)
        return cats
    }
}
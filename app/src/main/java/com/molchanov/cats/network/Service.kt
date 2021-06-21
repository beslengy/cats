package com.molchanov.cats.network

import com.molchanov.cats.network.networkmodels.*
import okhttp3.RequestBody
import retrofit2.http.*

interface CatsApiService {

    /**
     * Метод для получения всех картинок
     */
    @GET("images/search")
    suspend fun getAllImages(
        @QueryMap filter: Map<String, String>,
        @Query("limit") limit: Int,
        @Query("page") page: Int


    ): List<CatItem>

    /**
     * Метод для получения отдельного котика через его ID
     */
    @GET("images/{image_id}")
    suspend fun getCatByImage(@Path("image_id") imageId: String): CatDetail

    /**
     * Метод для добавления картинки в избранное
     */

    @POST("favourites")
    suspend fun postFavorite(
        @Body postFavorite: PostFavorite
    ): NetworkResponse

    /**
     * Метод для получения списка всех избранных картинок
     */
    @GET("favourites")
    suspend fun getAllFavorites(
        @Query("limit") limit: Int,
        @Query("page") page: Int
    ): List<CatItem>

    /**
     * Метод для удаления картинки из избранного
     */
    @DELETE("favourites/{favourite_id}")
    suspend fun deleteFavorite(@Path("favourite_id") favoriteId: String): NetworkResponse

    /**
     * Метод для получения моих загруженных картинок
     */
    @GET("images")
    suspend fun getAllUploaded(
        @Query("limit") limit: Int,
        @Query("page") page: Int
    ): List<CatItem>

    /**
     * Метод для загрузки картинки на сервер
     */
    @Multipart
    @POST("/images/upload")
    suspend fun uploadImage(
        @Part("file") file: RequestBody,
        @Part("sub_id") username: RequestBody
    ): NetworkResponse

    @GET("breeds")
    suspend fun getBreeds(): List<FilterItem>

    @GET("categories")
    suspend fun getCategories(): List<FilterItem>

    /**
     * Метод для удаления моей загруженной картинки с сервера
     * TODO: Метод для удаления моей загруженной картинки с сервера
     */

    /**
     * Метод для получения анализа картинки
     * TODO: Метод для получения анализа картинки
     */
}
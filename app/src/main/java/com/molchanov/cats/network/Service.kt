package com.molchanov.cats.network

import com.molchanov.cats.network.networkmodels.CatDetail
import com.molchanov.cats.network.networkmodels.CatItem
import com.molchanov.cats.network.networkmodels.NetworkResponse
import com.molchanov.cats.network.networkmodels.PostFavorite
import com.molchanov.cats.utils.API_KEY
import com.molchanov.cats.utils.BASE_URL
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

private val moshi = Moshi.Builder()
    .addLast(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()

interface CatsApiService {

    /**
     * Метод для получения всех картинок
     */
    @Headers(API_KEY)
    @GET("images/search?limit=20&page=1&include_favourite=0")
    suspend fun getAllImages() : List<CatItem>

    /**
     * Метод для получения отдельного котика через его ID
     */
    @Headers(API_KEY)
    @GET("images/{image_id}")
    suspend fun getCatByImage(@Path("image_id") imageId: String) : CatDetail

    /**
     * Метод для добавления картинки в избранное
     */

    @Headers(API_KEY)
    @POST("favourites")
    suspend fun postFavorite(
        @Body postFavorite: PostFavorite
    ) : NetworkResponse

    /**
     * Метод для получения списка всех избранных картинок по имени пользователя
     */
    @Headers(API_KEY)
    @GET("favourites")
    suspend fun getAllFavorites(@QueryMap options: Map<String, String>) : List<CatItem>

    /**
     * Метод для удаления картинки из избранного
     */
    @Headers(API_KEY)
    @DELETE("favourites/{favourite_id}")
    suspend fun deleteFavorite(@Path("favourite_id") favoriteId: String) : NetworkResponse

    /**
     * Метод для получения моих загруженных картинок
     */
    @Headers(API_KEY)
    @GET("images")
    suspend fun getAllUploaded(@QueryMap options: Map<String, String>) : List<CatItem>

    /**
     * Метод для загрузки картинки на сервер
     */
    @Headers(API_KEY)
    @Multipart
    @POST("/images/upload")
    suspend fun uploadImage(
        @Part("file") file: RequestBody,
        @Part("sub_id") username: RequestBody
    ): NetworkResponse

    /**
     * Метод для удаления моей загруженной картинки с сервера
     * TODO: Метод для удаления моей загруженной картинки с сервера
     */

    /**
     * Метод для получения анализа картинки
     * TODO: Метод для получения анализа картинки
     */
}

object CatsApi {
    val retrofitService : CatsApiService by lazy {
        retrofit.create(CatsApiService::class.java)
    }
}
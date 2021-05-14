package com.molchanov.cats.network

import com.molchanov.cats.utils.API_KEY
import com.molchanov.cats.utils.BASE_URL
import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*
import java.io.File

private val moshi = Moshi.Builder()
    .addLast(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()

interface CatsApiService {
//    @Headers(API_KEY)
//    @GET("breeds")
//    suspend fun getCats(): List<NetworkImages>


    /**
     * Метод для получения всех картинок
     */
    @Headers(API_KEY)
    @GET("images/search?limit=20&page=1&include_favourite=0")
    suspend fun getAllImages() : List<NetworkImage>

    /**
     * Метод для получения отдельного котика через его ID
     */
    @Headers(API_KEY)
    @GET("images/{image_id}")
    suspend fun getCatByImage(@Path("image_id") imageId: String) : NetworkCat

    /**
     * Метод для добавления картинки в избранное
     */

    @Headers(API_KEY)
//    @FormUrlEncoded
    @POST("favourites")
    suspend fun postFavorite(
        @Body postFavorite: PostFavorite
//        @Field("image_id")imageId: String
//        @Field("sub_id")username: String = USER_ID
    ) : ResponseFavorite

    /**
     * Метод для получения списка всех избранных картинок по имени пользователя
     */
    @Headers(API_KEY)
    @GET("favourites")
    suspend fun getAllFavorites(@QueryMap options: Map<String, String>) : List<NetworkFavorite>

    /**
     * Метод для удаления картинки из избранного
     */
    @Headers(API_KEY)
    @DELETE("favourites/{favourite_id}")
    suspend fun deleteFavorite(@Path("favourite_id") favoriteId: String) : ResponseFavorite

    /**
     * Метод для получения моих загруженных картинок
     */
    @Headers(API_KEY)
    @GET("/images/upload")
    suspend fun getAllUploaded(@Query("sub_id") username: String) : List<NetworkUploaded>

    /**
     * Метод для загрузки картинки на сервер
     */
    @Headers(API_KEY)
    @POST("/images/upload")
    suspend fun uploadImage(@Body file: File, @Json(name = "sub_id")username: String = com.molchanov.cats.utils.USER_ID )

    /**
     * Метод для удаления моей загруженной картинки с сервера
     */

    /**
     * Метод для получения анализа картинки
     */
}

object CatsApi {
    val retrofitService : CatsApiService by lazy {
        retrofit.create(CatsApiService::class.java)
    }
}
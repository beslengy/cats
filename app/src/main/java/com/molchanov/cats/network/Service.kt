package com.molchanov.cats.network

import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

private const val USER_ID = "user-17"
private const val API_KEY = "x-api-key: 177e2034-b213-4178-834f-a3d237cc68ad"
private const val BASE_URL = "https://api.thecatapi.com/v1/"

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


    @Headers(API_KEY)
    @GET("images/search?limit=50&page=50&include_favourite=0")
    suspend fun getAllImages() : List<NetworkImage>

    @Headers(API_KEY)
    @GET("images/{image_id}")
    suspend fun getCatByImage(@Path("image_id") imageId: String) : NetworkCat

    @Headers(API_KEY)
    @POST("favorites")
    suspend fun postFavorite(@Body @Json(name = "image_id")imageId: String, @Json(name = "sub_id")username: String = USER_ID) : String

    @Headers(API_KEY)
    @GET("favourites")
    suspend fun getAllFavorites() : List<NetworkFavorites>
}

object CatsApi {
    val retrofitService : CatsApiService by lazy {
        retrofit.create(CatsApiService::class.java)
    }
}
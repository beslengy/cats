package com.molchanov.cats.network

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers

private const val API_KEY = "x-api-key: 177e2034-b213-4178-834f-a3d237cc68ad"
private const val BASE_URL = "https://api.thecatapi.com/v1/"

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(ScalarsConverterFactory.create())
    .build()

interface CatsApiService {
    @Headers("x-api-key: 177e2034-b213-4178-834f-a3d237cc68ad")
    @GET("breeds")
    fun getCats(): Call<String>
}

object CatsApi {
    val retrofitService : CatsApiService by lazy {
        retrofit.create(CatsApiService::class.java)
    }
}
package com.molchanov.cats.di

import com.molchanov.cats.network.CatsApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private const val BASE_URL ="https://api.thecatapi.com/v1/"
    @Provides
    fun provideMoshi(): Moshi =
        Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()

    @Provides
    fun provideRetrofit(moshi: Moshi, client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()

    @Provides
    fun provideCatsApi(retrofit: Retrofit): CatsApiService =
        retrofit.create(CatsApiService::class.java)

    @Provides
    fun provideOkhttpClient(): OkHttpClient =
        OkHttpClient().newBuilder()
            .retryOnConnectionFailure(true)
            .addInterceptor {
                val request = it.request().newBuilder()
                    .header("x-api-key", "177e2034-b213-4178-834f-a3d237cc68ad")
                    .build()

                it.proceed(request)
            }
            .build()
}


package com.molchanov.cats.di

import androidx.recyclerview.widget.GridLayoutManager
import com.molchanov.cats.network.CatsApiService
import com.molchanov.cats.utils.APP_ACTIVITY
import com.molchanov.cats.utils.BASE_URL
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

    @Provides
    fun getLayoutManager(): GridLayoutManager = GridLayoutManager(APP_ACTIVITY, 2, GridLayoutManager.VERTICAL, false)

//    @Provides
//    @Named("voteUp")
//    fun getVoteUpButton() : ImageButton = APP_ACTIVITY.findViewById(R.id.btn_like)
//
//    @Provides
//    @Named("voteDown")
//    fun getVoteDownButton() : ImageButton = APP_ACTIVITY.findViewById(R.id.btn_dislike)

}


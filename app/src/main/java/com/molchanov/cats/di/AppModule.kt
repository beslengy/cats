package com.molchanov.cats.di

import android.widget.ImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.molchanov.cats.R
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
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideMoshi(): Moshi =
        Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(moshi: Moshi, client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()

    @Provides
    @Singleton
    fun provideCatsApi(retrofit: Retrofit): CatsApiService =
        retrofit.create(CatsApiService::class.java)

    @Provides
    @Singleton
    fun provideOkhttpClient(): OkHttpClient =
        OkHttpClient().newBuilder()
            .addInterceptor {

                val request = it.request().newBuilder()
                    .header("x-api-key", "177e2034-b213-4178-834f-a3d237cc68ad")
                    .build()

                it.proceed(request)
            }
            .build()

    @Provides
    fun getLayoutManager(): GridLayoutManager = GridLayoutManager(APP_ACTIVITY, 2, GridLayoutManager.VERTICAL, false)

    @Provides
    @Singleton
    fun getVoteLayout() : ConstraintLayout = APP_ACTIVITY.findViewById(R.id.vote_buttons_layout)

    @Provides
    @Singleton
    @Named("voteUp")
    fun getVoteUpButton() : ImageButton = APP_ACTIVITY.findViewById(R.id.btn_like)

    @Provides
    @Singleton
    @Named("voteDown")
    fun getVoteDownButton() : ImageButton = APP_ACTIVITY.findViewById(R.id.btn_dislike)

}


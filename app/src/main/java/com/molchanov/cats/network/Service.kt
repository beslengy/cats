package com.molchanov.cats.network

import com.molchanov.cats.network.networkmodels.*
import com.molchanov.cats.utils.USER_ID
import okhttp3.MultipartBody
import retrofit2.http.*

interface CatsApiService {

    /**
     * Метод для получения всех картинок
     */
    @GET("images/search")
    suspend fun getAllImages(
        @QueryMap filter: Map<String, String>,
        @Query("limit") limit: Int,
        @Query("page") page: Int,
        @Query("size") size: String = "med",
        @Query("sub_id") username: String = USER_ID,
        @Query("include_favourite") includeFavorite: Int = 1,
        @Query("include_vote") includeVote: Int = 1
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
        @Query("page") page: Int,
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
    @POST("images/upload")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part,
        @Part("sub_id") username: String = USER_ID
    ): NetworkResponse

    @GET("breeds")
    suspend fun getBreeds(): List<FilterItem>

    @GET("categories")
    suspend fun getCategories(): List<FilterItem>

    /**
     * Метод для удаления моей загруженной картинки с сервера
     */
    @DELETE("images/{imageId}")
    suspend fun deleteUploaded(@Path("imageId") imageId: String)

    /**
     * Метод для получения анализа картинки
     */
    @GET("images/{image_id}/analysis")
    suspend fun getAnalysis(@Path("image_id") imageId: String): List<Analysis?>

    /**
     * Метод для получения всех проголосованных котиков
     * TODO: Метод для получения анализа картинки
     */


    /**
     * Метод для голосования
     */
    @POST("votes")
    suspend fun postVote(
        @Body postVote: PostVote
    ): NetworkResponse
    /**
     * Метод для удаления голоса
     */
    @DELETE("votes/{vote_id}")
    suspend fun deleteVote(
        @Path("vote_id") voteId: String
    ) : NetworkResponse
}
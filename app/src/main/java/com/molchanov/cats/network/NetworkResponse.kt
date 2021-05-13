package com.molchanov.cats.network

import com.molchanov.cats.domain.Cat
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

private const val USER_ID = "user-17"

/**
 * Класс является моделью для изображений, возвращаемых по запросу "/image".
 */
@JsonClass(generateAdapter = true)
data class NetworkImage (
    @Json(name = "id")val imageId: String,
    @Json(name = "url") val imageUrl: String
)
@JsonClass(generateAdapter = true)
data class NetworkCat (
    val id: String,
    val url: String,
    @Json(name = "sub_id") val username: String? = null
)

@JsonClass(generateAdapter = true)
data class NetworkFavorite (
    @Json(name = "id") val favoriteId: String,
    @Json(name = "image_id") val imageId: String,
    @Json(name="created_at") val createdAt: String,
    @Json(name = "sub_id") val username: String,
    @Json(name = "image") val favImage: FavoriteImage
) {
    fun asDomainModel() : Cat {
        return this.let {
            Cat(
                imageId = it.imageId,
                imageUrl = it.favImage.imageUrl,
                username = it.username,
                favoriteId = it.favoriteId,
                filename = ""
            )
        }
    }
}
@JsonClass(generateAdapter = true)
data class FavoriteImage (
    @Json(name = "id")val imageId: String,
    @Json(name = "url") val imageUrl: String
)

@JsonClass(generateAdapter = true)
data class ResponseFavorite (
    val message: String
//    val id: Any = ""
)

@JsonClass(generateAdapter = true)
data class PostFavorite constructor (
    @Json(name = "image_id") val imageId: String,
    @Json(name = "sub_id") val username: String = USER_ID
)

@JsonClass(generateAdapter = true)
data class NetworkUploaded (
    @Json(name = "id") val imageId: String,
    @Json(name="created_at") val createdAt: String,
    @Json(name = "sub_id") val username: String
)
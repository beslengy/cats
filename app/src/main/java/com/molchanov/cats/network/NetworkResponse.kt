package com.molchanov.cats.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

private const val USER_ID = "user-17"

/**
 * Класс является моделью для изображений, возвращаемых по запросу "/image".
 *
 */

@JsonClass(generateAdapter = true)
data class NetworkImage (
    val id: String,
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
    @Json(name = "sub_id") val username: String
)

@JsonClass(generateAdapter = true)
data class ResponseFavorite (
    val message: String,
    val id: Any
        )

@JsonClass(generateAdapter = true)
data class PostFavorite constructor (
    @Json(name = "image_id") val imageId: String,
    @Json(name = "sub_id") val username: String = USER_ID
        )

@JsonClass(generateAdapter = true)
data class NetworkUploaded (
    @Json(name = "id") val favoriteId: String,
    @Json(name = "id") val imageId: String,
    @Json(name="created_at") val createdAt: String,
    @Json(name = "sub_id") val username: String
)

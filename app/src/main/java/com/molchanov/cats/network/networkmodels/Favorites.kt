package com.molchanov.cats.network.networkmodels

import com.molchanov.cats.domain.Cat
import com.molchanov.cats.utils.USER_ID
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkFavorite(
    @Json(name = "id") val favoriteId: String,
    @Json(name = "image_id") val imageId: String,
    @Json(name = "created_at") val createdAt: String,
    @Json(name = "sub_id") val username: String,
    @Json(name = "image") val favImage: FavoriteImage
) {
    fun asDomainModel(): Cat {
        return Cat.Builder()
            .imageId(this.imageId)
            .favoriteId(this.favoriteId)
            .username(this.username)
            .imageUrl(this.favImage.imageUrl)
            .build()
    }
}

@JsonClass(generateAdapter = true)
data class FavoriteImage(
    @Json(name = "id") val imageId: String,
    @Json(name = "url") val imageUrl: String
)

@JsonClass(generateAdapter = true)
data class PostFavorite constructor(
    @Json(name = "image_id") val imageId: String,
    @Json(name = "sub_id") val username: String = USER_ID
)
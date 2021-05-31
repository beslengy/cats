package com.molchanov.cats.network.networkmodels

import com.molchanov.cats.domain.Cat
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkUploaded (
    @Json(name = "id") val imageId: String,
    @Json(name="created_at") val createdAt: String,
    @Json(name = "url") val imageUrl: String,
    @Json(name = "sub_id") val username: String,
    @Json(name = "original_filename") val filename: String
) {
    fun asDomainModel(): Cat {
        return Cat.Builder()
            .imageId(this.imageId)
            .imageUrl(this.imageUrl)
            .username(this.username)
            .favoriteId("")
            .filename(this.filename)
            .build()

    }
}
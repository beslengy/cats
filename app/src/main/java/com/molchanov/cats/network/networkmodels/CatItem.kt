package com.molchanov.cats.network.networkmodels

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CatItem(
    val id: String,
    val url: String? = null,
    val image: Image? = null,
    @Json(name = "original_filename") val filename: String? = null,
    var vote: Vote? = null,
    var favourite: Favourite? = null,
) {
    data class Vote(
        @Json(name = "id") val voteId: String,
        val value: Int,
    )

    data class Favourite(
        @Json(name = "id") val favId: String,
    )

    data class Image(
        val id: String,
        val url: String,
    )

    val imageUrl: String = url ?: image!!.url
    var isFavorite = (favourite != null || image != null)
}
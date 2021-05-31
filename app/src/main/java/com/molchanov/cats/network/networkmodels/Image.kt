package com.molchanov.cats.network.networkmodels

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Image(
    @Json(name="id") val imageId: String,
    @Json(name="url") val imageUrl: String
)

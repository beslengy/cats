package com.molchanov.cats.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkImage (
    val id: String,
    @Json(name = "url") val imageUrl: String
)

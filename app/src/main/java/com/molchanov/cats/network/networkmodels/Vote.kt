package com.molchanov.cats.network.networkmodels

import com.squareup.moshi.Json

data class Vote(
    @Json(name = "id") val voteId: String,
    val value: Int,
    @Json(name="image_id") val imageId: String
)

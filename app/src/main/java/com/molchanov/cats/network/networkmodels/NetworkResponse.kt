package com.molchanov.cats.network.networkmodels

import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class NetworkResponse (
    val message: String
)
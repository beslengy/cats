package com.molchanov.cats.network.networkmodels

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FilterItem(
    val id: String,
    val name: String
)

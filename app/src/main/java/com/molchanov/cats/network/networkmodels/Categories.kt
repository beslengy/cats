package com.molchanov.cats.network.networkmodels

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Categories (
    val id: Int,
    val name: String
)

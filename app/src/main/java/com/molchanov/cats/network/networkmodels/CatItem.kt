package com.molchanov.cats.network.networkmodels

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CatItem(
    val id: String,
    val url: String? = null,
    val image: Image? = null
) {
    data class Image(
        val id: String,
        val url: String
    )
    val imageUrl: String = url ?: image!!.url
}
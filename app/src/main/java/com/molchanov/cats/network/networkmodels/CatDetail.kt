package com.molchanov.cats.network.networkmodels

import com.squareup.moshi.Json

data class CatDetail(
    val breeds: List<Breeds>? = null,
    val categories: List<Categories>? = null,
    val id: String,
    val url: String
) {
    data class Breeds(
        @Json(name = "alt_names")
        val altNames: String? = null,
        @Json(name = "name")
        val name: String? = null,
        @Json(name = "origin")
        val origin: String? = null,
        @Json(name = "temperament")
        val temperament: String? = null,
        @Json(name = "description")
        val description: String? = null,

        )

    data class Categories(
        val id: String
    )
}

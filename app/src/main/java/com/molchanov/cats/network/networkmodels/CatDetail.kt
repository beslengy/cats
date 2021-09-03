package com.molchanov.cats.network.networkmodels

import com.squareup.moshi.Json

data class CatDetail(
    val breeds: List<Breeds>? = null,
    val categories: List<Categories>? = null,
    val id: String,
    @Json(name = "url")
    val imageUrl: String
) {
    data class Breeds(
        @Json(name = "alt_names")
        val altNames: String? = null,
        @Json(name = "name")
        val breedName: String? = null,
        @Json(name = "origin")
        val origin: String? = null,
        @Json(name = "temperament")
        val temperament: String? = null,
        @Json(name = "description")
        val description: String? = null,
        @Json(name = "life_span")
        val lifeSpan: String? = null,
        @Json(name = "indoor")
        val indoor: Double,
        @Json(name = "child_friendly")
        val childFriendly: Int? = null,
        @Json(name = "cat_friendly")
        val catFriendly: Int? = null,
        @Json(name = "dog_friendly")
        val dogFriendly: Int? = null,
        @Json(name = "energy_level")
        val energyLevel: Int? = null,
        @Json(name = "grooming")
        val grooming: Int? = null,
        @Json(name = "health_issues")
        val healthIssues: Int? = null,
        @Json(name = "intelligence")
        val intelligence: Int? = null,
        @Json(name = "shedding_level")
        val sheddingLevel: Int? = null,
        @Json(name = "social_needs")
        val socialNeeds: Int? = null,
        @Json(name = "stranger_friendly")
        val strangerFriendly: Int? = null,
        @Json(name = "vocalisation")
        val vocalisation: Int? = null,
    )

    data class Categories(
        val id: String,
        val name: String
    )
}

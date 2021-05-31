package com.molchanov.cats.network.networkmodels

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
@JsonClass(generateAdapter = true)
data class BreedWrapper(
    val breeds: List<Breeds>
)
@JsonClass(generateAdapter = true)
data class Breeds(
    @Json(name = "alt_names") val altNames: String? = null,
    @Json(name = "experimental") val experimental: Int? = null,
    @Json(name = "hairless") val hairless: Int? = null,
    @Json(name = "hypoallergenic") val hypoallergenic: Int? = null,
    @Json(name = "id") val id: String? = null,
    @Json(name = "life_span") val lifeSpan: String? = null,
    @Json(name = "name") val name: String? = null,
    @Json(name = "natural") val natural: Int? = null,
    @Json(name = "origin") val origin: String? = null,
    @Json(name = "rare") val rare: Int? = null,
    @Json(name = "reference_image_id") val referenceImageId: String? = null,
    @Json(name = "rex") val rex: Int? = null,
    @Json(name = "short_legs") val shortLegs: Int? = null,
    @Json(name = "suppressed_tail") val suppressedTail: Int? = null,
    @Json(name = "temperament") val temperament: String? = null,
    @Json(name = "weight_imperial") val weightImperial: String? = null,
    @Json(name = "wikipedia_url") val wikiUrl: String? = null,
    val description: String? = null,
    val image: Image? = null,
    val weight: Weight? = null
)
@JsonClass(generateAdapter = true)
data class Weight(
    val imperial: String? = null,
    val metric: String? = null
)
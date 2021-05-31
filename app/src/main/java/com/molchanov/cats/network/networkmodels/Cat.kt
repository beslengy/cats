package com.molchanov.cats.network.networkmodels

import com.molchanov.cats.domain.Cat
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkCat (
    @Json(name = "id")val imageId: String,
    @Json(name = "url") val imageUrl: String,
    @Json(name = "sub_id") val username: String = "",
    val breeds: List<Breeds>? = null

){
    fun asDomainModel(): Cat {
        return Cat.Builder()
            .imageId(this.imageId)
            .imageUrl(this.imageUrl)
            .username(this.username)
            .name(this.breeds?.get(0)?.name)
            .altNames(this.breeds?.get(0)?.altNames)
            .description(this.breeds?.get(0)?.description)
            .build()
    }
}
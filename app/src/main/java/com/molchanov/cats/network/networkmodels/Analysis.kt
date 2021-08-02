package com.molchanov.cats.network.networkmodels

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Analysis(
    @Json(name = "image_id") val imageId: String,
    val vendor: String,
    @Json(name = "created_at") val createdAt: String,
    val labels: List<Label>,
    var imageUrl : String = ""
) : Parcelable {
    @Parcelize
    @JsonClass(generateAdapter = true)
    data class Label(
        @Json(name = "Name") val name: String,
        @Json(name = "Confidence") val confidence: Double
    ) : Parcelable
}

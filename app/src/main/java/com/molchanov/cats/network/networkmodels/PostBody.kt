package com.molchanov.cats.network.networkmodels

import com.molchanov.cats.utils.USER_ID
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import okhttp3.MultipartBody

@JsonClass(generateAdapter = true)
data class PostFavorite constructor(
    @Json(name = "image_id") val imageId: String,
    @Json(name = "sub_id") val username: String = USER_ID
)

@JsonClass(generateAdapter = true)
data class PostUploaded constructor(
    @Json(name = "file") val file: MultipartBody.Part,
    @Json(name = "sub_id") val username: String = USER_ID
)
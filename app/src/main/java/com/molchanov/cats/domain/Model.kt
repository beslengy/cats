package com.molchanov.cats.domain

data class Cat(
    val imageId: String,
    val imageUrl: String,
    val username: String,
    val favoriteId: String,
    val filename: String
){
     var isUploaded: Boolean = filename != ""
    var isFavorites: Boolean = favoriteId != ""
}

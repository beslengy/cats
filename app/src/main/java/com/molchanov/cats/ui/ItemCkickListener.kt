package com.molchanov.cats.ui

import com.molchanov.cats.network.NetworkImage

class ItemClickListener(
    val imageClicklistener : (selectedImage: NetworkImage) -> Unit,
    val favoriteClicklistener : (selectedImage: NetworkImage) -> Unit) {
    fun onItemClicked(selectedImage: NetworkImage) = imageClicklistener(selectedImage)
    fun onFavoriteBtnClicked(selectedImage: NetworkImage) = favoriteClicklistener(selectedImage)
}
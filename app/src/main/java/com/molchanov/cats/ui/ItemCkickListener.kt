package com.molchanov.cats.ui

import com.molchanov.cats.domain.Cat

class ItemClickListener(
    val imageClicklistener : (selectedImage: Cat) -> Unit,
    val favoriteClicklistener : (selectedImage: Cat) -> Unit) {
    fun onItemClicked(selectedImage: Cat) = imageClicklistener(selectedImage)
    fun onFavoriteBtnClicked(selectedImage: Cat) = favoriteClicklistener(selectedImage)
}
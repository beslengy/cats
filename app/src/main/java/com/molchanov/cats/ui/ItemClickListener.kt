package com.molchanov.cats.ui

import com.molchanov.cats.network.networkmodels.CatItem

interface ItemClickListener {
    fun onItemClicked(selectedImage: CatItem)
    fun onFavoriteBtnClicked(selectedImage: CatItem)
}
//class ItemClickListener(
//    val imageClickListener : (selectedImage: CatItem) -> Unit,
//    val favoriteClickListener : (selectedImage: CatItem) -> Unit) {
//    fun onItemClicked(selectedImage: CatItem) = imageClickListener(selectedImage)
//    fun onFavoriteBtnClicked(selectedImage: CatItem) = favoriteClickListener(selectedImage)
//}
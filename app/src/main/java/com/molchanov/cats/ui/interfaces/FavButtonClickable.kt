package com.molchanov.cats.ui.interfaces

import com.molchanov.cats.network.networkmodels.CatItem

interface FavButtonClickable : ItemClickable {
    fun onFavoriteBtnClicked(selectedImage: CatItem)
}
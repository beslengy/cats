package com.molchanov.cats.ui.interfaces

import com.molchanov.cats.network.networkmodels.CatItem

interface FavButtonClickable {
    fun onFavoriteBtnClicked(selectedImage: CatItem)
}
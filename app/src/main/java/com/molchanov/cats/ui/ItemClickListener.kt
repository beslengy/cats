package com.molchanov.cats.ui

import com.molchanov.cats.network.networkmodels.CatItem

interface ItemClickListener {
    fun onItemClicked(selectedImage: CatItem)
    fun onItemLongTap(selectedImage: CatItem)
    fun onFavoriteBtnClicked(selectedImage: CatItem)
}
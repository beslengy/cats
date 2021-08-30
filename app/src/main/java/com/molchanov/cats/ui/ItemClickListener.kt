package com.molchanov.cats.ui

import android.widget.ImageView
import com.molchanov.cats.network.networkmodels.CatItem

interface ItemClickListener {
    fun onItemClicked(selectedImage: CatItem, imageView: ImageView)
    fun onItemLongTap(selectedImage: CatItem)
    fun onFavoriteBtnClicked(selectedImage: CatItem)
}
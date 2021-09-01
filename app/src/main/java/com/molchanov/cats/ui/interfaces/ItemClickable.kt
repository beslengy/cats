package com.molchanov.cats.ui.interfaces

import android.widget.ImageView
import com.molchanov.cats.network.networkmodels.CatItem

interface ItemClickable {
    fun onItemClicked(selectedImage: CatItem, imageView: ImageView)
}
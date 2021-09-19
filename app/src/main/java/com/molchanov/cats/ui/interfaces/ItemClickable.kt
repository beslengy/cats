package com.molchanov.cats.ui.interfaces

import android.widget.ImageView
import com.google.android.material.card.MaterialCardView
import com.molchanov.cats.network.networkmodels.CatItem

interface ItemClickable {
    fun onItemClicked(selectedImage: CatItem, imageView: ImageView, itemView: MaterialCardView)
}
package com.molchanov.cats.ui.interfaces

import com.molchanov.cats.network.networkmodels.CatItem

interface LongTappable {
    fun onItemLongTap(selectedImage: CatItem)
}
package com.molchanov.cats.ui

import androidx.recyclerview.widget.DiffUtil
import com.molchanov.cats.network.networkmodels.CatItem

class DiffCallback : DiffUtil.ItemCallback<CatItem>() {

    override fun areItemsTheSame(oldItem: CatItem, newItem: CatItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CatItem, newItem: CatItem): Boolean {
        return oldItem == newItem
    }

}
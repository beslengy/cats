package com.molchanov.cats.ui

import androidx.recyclerview.widget.DiffUtil
import com.molchanov.cats.domain.Cat

class DiffCallback : DiffUtil.ItemCallback<Cat>() {

    override fun areItemsTheSame(oldItem: Cat, newItem: Cat): Boolean {
        return oldItem.imageId == newItem.imageId
        //oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: Cat, newItem: Cat): Boolean {
        return oldItem == newItem
        //oldItem.imageId == newItem.imageId
    }

}
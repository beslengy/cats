package com.molchanov.cats.ui

import androidx.recyclerview.widget.DiffUtil
import com.molchanov.cats.network.NetworkImage

class DiffCallback : DiffUtil.ItemCallback<NetworkImage>() {
    override fun areItemsTheSame(oldItem: NetworkImage, newItem: NetworkImage): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: NetworkImage, newItem: NetworkImage): Boolean {
        return oldItem.id == newItem.id
    }

}
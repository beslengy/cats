package com.molchanov.cats.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.molchanov.cats.databinding.ImageItemBinding
import com.molchanov.cats.network.networkmodels.CatItem

class ImageItemAdapter (private val itemClickListener: ItemClickListener) : ListAdapter<CatItem, ImageItemAdapter.ViewHolder>(DiffCallback()) {

    class ViewHolder private constructor(private val binding: ImageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(image: CatItem, itemClickListener: ItemClickListener) {
            binding.clicklistener = itemClickListener
            binding.image = image
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {

                return ViewHolder(
                    ImageItemBinding.inflate(
                        LayoutInflater.from(parent.context)
                    )
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        holder.bind(item, itemClickListener)

    }
}






package com.molchanov.cats.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.molchanov.cats.databinding.ImageItemBinding
import com.molchanov.cats.network.networkmodels.CatItem
import com.molchanov.cats.utils.bindImage

class PageAdapter(private val itemClickListener: ItemClickListener) :
    PagingDataAdapter<CatItem, PageAdapter.ViewHolder>(
        COMPARATOR
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item, itemClickListener)
        }
    }

    class ViewHolder private constructor(private val binding: ImageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(image: CatItem, itemClickListener: ItemClickListener) {
            binding.apply {
                ivImageItem.bindImage(image.imageUrl)

                btnFavorites.setOnClickListener {
                    itemClickListener.onFavoriteBtnClicked(image)
                }
                ivImageItem.setOnClickListener {
                    itemClickListener.onItemClicked(image)
                }
            }
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

    companion object {
        //чтобы создать экземпляр абстрактного класса добавляем "object :"
        private val COMPARATOR = object : DiffUtil.ItemCallback<CatItem>() {

            override fun areItemsTheSame(oldItem: CatItem, newItem: CatItem) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: CatItem, newItem: CatItem) =
                oldItem == newItem
        }
    }
}
package com.molchanov.cats.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.molchanov.cats.databinding.ImageItemBinding
import com.molchanov.cats.network.NetworkImage

class HomeAdapter (val itemClickListener: ItemClickListener) : ListAdapter<NetworkImage, HomeAdapter.ViewHolder>(NetworkImageDiffCallback()) {


    class ViewHolder private constructor(private val binding : ImageItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(image: NetworkImage, itemClickListener: ItemClickListener) {
            binding.clicklistener = itemClickListener
            binding.image = image
            binding.executePendingBindings()
        }
        companion object {
            fun from(parent: ViewGroup): ViewHolder {

                return ViewHolder(ImageItemBinding.inflate(
                    LayoutInflater.from(parent.context)
                ))
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
class NetworkImageDiffCallback : DiffUtil.ItemCallback<NetworkImage>() {
    override fun areItemsTheSame(oldItem: NetworkImage, newItem: NetworkImage): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: NetworkImage, newItem: NetworkImage): Boolean {

        return oldItem.id == newItem.id
    }

}

class ItemClickListener(val clicklistener : (selectedImage: NetworkImage) -> Unit) {
    fun onItemClicked(selectedImage: NetworkImage) = clicklistener(selectedImage)
}



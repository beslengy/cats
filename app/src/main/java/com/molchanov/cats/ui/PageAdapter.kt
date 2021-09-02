package com.molchanov.cats.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.res.ResourcesCompat.getDrawable
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.molchanov.cats.R
import com.molchanov.cats.databinding.MainItemBinding
import com.molchanov.cats.network.networkmodels.CatItem
import com.molchanov.cats.ui.interfaces.FavButtonClickable
import com.molchanov.cats.ui.interfaces.LongTappable
import com.molchanov.cats.utils.bindImage

class PageAdapter(
    private val favButtonClickListener: FavButtonClickable? = null,
    private val longTapClickListener: LongTappable? = null
) : PagingDataAdapter<CatItem, PageAdapter.ViewHolder>(
        COMPARATOR
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            MainItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it, favButtonClickListener, longTapClickListener)
        }
    }

    inner class ViewHolder(private val binding: MainItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(image: CatItem, favButtonClickListener: FavButtonClickable?, longTapClickListener: LongTappable?) {
            binding.apply {
                ivImageItem.bindImage(image.imageUrl)
                imageCard.transitionName = "cat_card_transition_name_${image.id}"
                ivImageItem.transitionName = "cat_image_transition_name_${image.url}"
                btnFavorites.apply {
                    if (image.isUploaded) this.visibility = View.GONE
                    if (image.isFavorite) this.setImageDrawable(getDrawable(resources,
                        R.drawable.ic_heart,
                        context.theme))
                }
                favButtonClickListener?.let {
                    ivImageItem.apply {
                        setOnClickListener {
                            favButtonClickListener.onItemClicked(image, this, binding.imageCard)
                        }
                    }
                    btnFavorites.apply {
                        setOnClickListener {
                            favButtonClickListener.onFavoriteBtnClicked(image)
                            val favIcon =
                                if (!image.isFavorite) R.drawable.ic_heart
                                else R.drawable.ic_heart_border
                            this.setImageDrawable(getDrawable(resources, favIcon, context.theme))
                        }
                    }
                }
                longTapClickListener?.let {
                    ivImageItem.apply {
                        setOnClickListener {
                            longTapClickListener.onItemClicked(image,this, binding.imageCard)
                        }
                        setOnLongClickListener {
                            val popup = PopupMenu(context, this)
                            popup.apply {
                                inflate(R.menu.delete_uploaded_menu)
                                setOnMenuItemClickListener {
                                    longTapClickListener.onItemLongTap(image)
                                    true
                                }
                            }
                            popup.show()
                            true
                        }
                    }
                }
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
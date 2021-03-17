package com.molchanov.cats

import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide


@BindingAdapter("imageUrl")
fun bindImage(view: ImageView, imageUrl: String?) {
    imageUrl?.let{
        val imgUri = it.toUri().buildUpon().scheme("https").build()
        Glide.with(view.context)
            .load(imgUri)
            .into(view)
    }
}
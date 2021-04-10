package com.molchanov.cats

import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.molchanov.cats.network.NetworkImage
import com.molchanov.cats.ui.home.HomeAdapter


@BindingAdapter("imageUrl")
fun ImageView.bindImage(imageUrl: String?) {
    imageUrl?.let{
        val imgUri = it.toUri().buildUpon().scheme("https").build()
        val circularProgressDrawable = CircularProgressDrawable(this.context)
        circularProgressDrawable.apply {
            strokeWidth = 10f
            centerRadius = 30f
            start()
        }

        Glide.with(this.context)
            .load(imgUri)
            .apply(RequestOptions()
                .placeholder(circularProgressDrawable)
                //.placeholder(R.drawable.loading_animation)
                .error(R.drawable.ic_broken_image))
            .into(this)
    }
}
@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<NetworkImage>?) {
    val adapter = recyclerView.adapter as HomeAdapter
    adapter.submitList(data)
}


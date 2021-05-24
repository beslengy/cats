package com.molchanov.cats.utils

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.molchanov.cats.R
import com.molchanov.cats.domain.Cat
import com.molchanov.cats.ui.ImageItemAdapter


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
fun bindRecyclerView(recyclerView: RecyclerView, data: MutableList<Cat>?) {
    Log.d("M_BindingAdapters", "bindRecyclerView вызван")
    val adapter = recyclerView.adapter as ImageItemAdapter
    adapter.submitList(data)
    Log.d("M_BindingAdapters", "$data")
    Log.d("M_BindingAdapters", "adapter.submitList вызван")
    Log.d("M_BindingAdapters", "${adapter.itemCount}")
}

@BindingAdapter("ApiStatusImage")
fun bindStatusImage(statusImageView: ImageView,
                    status: ApiStatus) {
    when(status) {
        ApiStatus.LOADING -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.loading_animation)
        }
        ApiStatus.ERROR -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_connection_error)
        }
        ApiStatus.DONE -> {
            statusImageView.visibility = View.GONE
        }
        ApiStatus.EMPTY -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_empty_list)
        }
    }
}

@BindingAdapter("ApiStatusText")
fun bindStatusText(statusTextView: TextView, status: ApiStatus){
    when(status) {
        ApiStatus.EMPTY -> {
            statusTextView.visibility = View.VISIBLE
            statusTextView.text = "Список пуст"
        }
        ApiStatus.ERROR -> {
            statusTextView.visibility = View.VISIBLE
            statusTextView.text = "Ошибка соединения"
        }
        ApiStatus.LOADING -> {
            statusTextView.visibility = View.GONE
        }
        ApiStatus.DONE -> {
            statusTextView.visibility = View.GONE
        }
    }
}



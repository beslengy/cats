package com.molchanov.cats.utils

import android.os.Build
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.core.text.HtmlCompat
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
    imageUrl?.let {
        val imgUri = it.toUri().buildUpon().scheme("https").build()
        val circularProgressDrawable = CircularProgressDrawable(this.context)
        circularProgressDrawable.apply {
            strokeWidth = 10f
            centerRadius = 30f
            start()
        }

        Glide.with(this.context)
            .load(imgUri)
            .apply(
                RequestOptions()
                    .placeholder(circularProgressDrawable)
                    //.placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_broken_image)
            )
            .into(this)
    }
}

@BindingAdapter("cardTextContent")
fun TextView.bindCardText(data: Cat?) {
    Log.d("M_BindingAdapters", "$data")
    data?.let {
        val sb = StringBuilder()
        sb.apply {
            append("<h3>CAT INFO<h3>")
            if (data.name != null) {
                append("<b>Name:</b>")
                append("\t${it.name}<br>")
                append("<br>")
            }
            if (data.altNames != null) {
                append("<b>Alternative names:</b>")
                append("\t${it.altNames}<br>")
                append("<br>")
            }
            if (data.description != null) {
                append("<b>Description:</b><br>")
                append("${it.description}<br>")
                append("<br>")
            }

        }
        text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(sb.toString(), Html.FROM_HTML_MODE_LEGACY)
        } else {
            HtmlCompat.fromHtml(sb.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)
        }
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
fun bindStatusImage(
    statusImageView: ImageView,
    status: ApiStatus
) {
    when (status) {
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
fun bindStatusText(statusTextView: TextView, status: ApiStatus) {
    when (status) {
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



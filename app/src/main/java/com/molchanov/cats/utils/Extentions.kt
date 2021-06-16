package com.molchanov.cats.utils

import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.molchanov.cats.R
import com.molchanov.cats.network.networkmodels.CatDetail


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
//            .transition(DrawableTransitionOptions.withCrossFade())
            .apply(
                RequestOptions()
                    .placeholder(circularProgressDrawable)
                    //.placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_broken_image)
            )
            .into(this)
    }
}

fun TextView.bindCardText(data: CatDetail?) {
    Log.d("M_BindingAdapters", "$data")
    data?.breeds?.get(0)?.let {
        text =
            StringBuilder().apply {
                append("CAT INFO\n")
                if (it.name != null) {
                    append("Name: ")
                    append("\t${it.name}\n")
                    append("\n")
                }
                if (it.altNames != null) {
                    append("Alternative names: ")
                    append("\t${it.altNames}\n")
                    append("\n")
                }
                if (it.temperament != null) {
                    append("Temperament: ")
                    append("${it.temperament}\n")
                    append("\n")
                }
                if (it.description != null) {
                    append("Description: \n")
                    append("${it.description}\n")
                    append("\n")
                }

            }
    }
}




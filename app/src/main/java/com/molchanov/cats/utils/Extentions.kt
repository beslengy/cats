package com.molchanov.cats.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.core.content.res.use
import androidx.core.net.toUri
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.molchanov.cats.R
import com.molchanov.cats.network.networkmodels.Analysis
import com.molchanov.cats.network.networkmodels.CatDetail

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.showToast(resId: Int) {
    Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()
}

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
                    .error(R.drawable.ic_broken_image)
            )
            .into(this)
    }
}

fun TextView.setAnalysisText(data: Analysis?) {
    data?.labels?.let { labels ->
        text =
            StringBuilder().apply {
                for (label in labels) {
                    appendLine("${label.name}: ${label.confidence.toInt()}%\n")
                }
            }
    }
}

fun TextView.setCardText(data: CatDetail?) {
    data?.breeds?.get(0)?.let {
        text =
            StringBuilder().apply {
                if (!it.breedName.isNullOrEmpty()) {
                    append("Name: ")
                    appendLine("\t${it.breedName}\n")
                }
                if (!it.altNames.isNullOrEmpty()) {
                    append("Alternative names: ")
                    appendLine("\t${it.altNames}\n")
                }
                if (!it.temperament.isNullOrEmpty()) {
                    append("Temperament: ")
                    appendLine("${it.temperament}\n")
                }
                if (!it.description.isNullOrEmpty()) {
                    appendLine("Description:")
                    appendLine("${it.description}\n")
                }
            }
    }
}

/**
 * Retrieve a color from the current [android.content.res.Resources.Theme].
 */
@ColorInt
@SuppressLint("Recycle")
fun Context.themeColor(
    @AttrRes themeAttrId: Int
): Int {
    return obtainStyledAttributes(
        intArrayOf(themeAttrId)
    ).use {
        it.getColor(0, Color.MAGENTA)
    }
}




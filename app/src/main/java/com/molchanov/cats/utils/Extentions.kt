package com.molchanov.cats.utils

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
    data?.breeds?.get(0)?.let {
        text =
            StringBuilder().apply {
                appendLine("CAT INFO")
                if (!it.name.isNullOrEmpty()) {
                    append("Name: ")
                    appendLine("\t${it.name}\n")
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

//fun ImageButton.setVoteUpButton(voteState: VoteStates) {
//    when(voteState) {
//        VoteStates.VOTE_UP -> this.apply{
//
//        }
//    }
//}




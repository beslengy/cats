package com.molchanov.cats.utils

import android.widget.Toast
import com.molchanov.cats.domain.Cat
import com.molchanov.cats.network.networkmodels.NetworkImage

fun showToast(message:String) {
    Toast.makeText(APP_ACTIVITY, message, Toast.LENGTH_SHORT).show()
}
fun List<NetworkImage>.asDomainModel(): List<Cat> {
    return map {
        Cat.Builder()
            .imageId(it.imageId)
            .imageUrl(it.imageUrl)
            .username("")
            .favoriteId("")
            .filename("")
            .build()

    }
}

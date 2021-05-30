package com.molchanov.cats.utils

import android.content.Intent
import android.provider.MediaStore
import android.widget.Toast
import com.molchanov.cats.domain.Cat
import com.molchanov.cats.network.NetworkImage

fun showToast(message:String) {
    Toast.makeText(APP_ACTIVITY, message, Toast.LENGTH_SHORT).show()
}
fun List<NetworkImage>.asDomainModel(): List<Cat> {
    return map {
        Cat(
            imageId = it.imageId,
            imageUrl = it.imageUrl,
            username = "",
            favoriteId = "",
            filename = ""
        )
    }
}
//private const val REQUEST_TAKE_PHOTO = 0
//private const val REQUEST_SELECT_IMAGE_IN_ALBUM = 1

fun selectImageInAlbum() {
    val intent = Intent(Intent.ACTION_PICK)
    intent.type = "image/*"
    val chooser = Intent.createChooser(intent, "Выберите изображение")
//    if (intent.resolveActivity(APP_ACTIVITY.packageManager) != null) {
//        startActivityForResult(APP_ACTIVITY, intent, REQUEST_SELECT_IMAGE_IN_ALBUM, null)
//    }
    APP_ACTIVITY.startActivity(chooser)
}
fun takePhoto() {
    val intent1 = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//    if (intent1.resolveActivity(APP_ACTIVITY.packageManager) != null) {
//        startActivityForResult(APP_ACTIVITY, intent1, REQUEST_TAKE_PHOTO, null)
//}
        APP_ACTIVITY.startActivity(intent1)

}
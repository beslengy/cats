package com.molchanov.cats.utils

import android.os.Environment
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*



/**
 * Метод для создания пустого файла изображения (без самого изображения).
 * Задаем имя исходя из текущего времени и даты.
 * @return [File]
 */
@Throws(IOException::class)
 fun createImageFile(): File {
    // Create an image file name
    val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ROOT).format(Date())
    val storageDir: File? = APP_ACTIVITY.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(
        "JPEG_${timeStamp}_", /* prefix */
        ".jpg", /* suffix */
        storageDir /* directory */
    ).apply {
        // Save a file: path for use with ACTION_VIEW intents
         CURRENT_PHOTO_PATH = absolutePath
    }
}



///**
// * Метод для выбора картинки из галереи.
// * Записывает путь файла в [CURRENT_PHOTO_PATH]
// */
//fun takeImageInAlbum(): Intent{
//    return Intent().apply {
//        action = Intent.ACTION_PICK
//        type = "image/*"
//    }.also { selectImageIntent ->
//        CURRENT_PHOTO_PATH = selectImageIntent.data?.path.toString()
//    }
//    val intent = Intent(Intent.ACTION_PICK)
//    intent.type = "image/*"
//    val chooser = Intent.createChooser(intent, "Выберите изображение")
////    if (intent.resolveActivity(APP_ACTIVITY.packageManager) != null) {
////        startActivityForResult(APP_ACTIVITY, intent, REQUEST_SELECT_IMAGE_IN_ALBUM, null)
////    }
//    APP_ACTIVITY.startActivity(chooser)
//}
//
//@SuppressLint("QueryPermissionsNeeded")
//fun takePhoto() {
////    val intent1 = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
////    if (intent1.resolveActivity(APP_ACTIVITY.packageManager) != null) {
////        startActivityForResult(APP_ACTIVITY, intent1, REQUEST_TAKE_PHOTO, null)
////}
//    Intent(MediaStore.ACTION_IMAGE_CAPTURE).also{ takePhotoIntent ->
//        takePhotoIntent.resolveActivity(APP_ACTIVITY.packageManager)?.also {
//            val photoFile : File? = try {
//                createImageFile()
//            } catch (e: IOException) {
//                Log.d("M_CatImagePicker", "ошибка при попытке создания файла: ${e.message}")
//                null
//            }
//            photoFile?.also {
//                val photoURI: Uri = FileProvider.getUriForFile(
//                    APP_ACTIVITY,
//                    "com.molchanov.cats",
//                    it
//                )
//                takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
//                startActivityForResult(APP_ACTIVITY, takePhotoIntent, REQUEST_TAKE_PHOTO, null)
//            }
//        }
//    }
//}




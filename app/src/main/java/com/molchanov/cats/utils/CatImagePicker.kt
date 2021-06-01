package com.molchanov.cats.utils

import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.core.content.FileProvider
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
    // Создаем имя файла, исходя из текущих даты и времени
    val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ROOT).format(Date())
    val storageDir: File? = APP_ACTIVITY.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(
        "JPEG_${timeStamp}_", /* prefix */
        ".jpg", /* suffix */
        storageDir /* directory */
    ).apply {
        // Сохраняем путь к файлу в глобальную переменную
        CURRENT_PHOTO_PATH = absolutePath
        Log.d("M_CatImagePicker", "Absolute path: $CURRENT_PHOTO_PATH")
    }
}

/**
 * Метод создает пустой файл, используя [createImageFile]
 * и возвращает его Uri через [FileProvider.getUriForFile]
 *
 * @return [Uri]
 */
fun getNewImageUri(): Uri {
    val file = createImageFile()
    return FileProvider.getUriForFile(
        APP_ACTIVITY,
        FILE_AUTHORITY,
        file
    )
}








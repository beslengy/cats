package com.molchanov.cats.utils

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import com.molchanov.cats.BuildConfig
import com.molchanov.cats.utils.Global.CURRENT_PHOTO_PATH
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class CatImagePicker {
    companion object {
        private const val FILE_AUTHORITY = "${BuildConfig.APPLICATION_ID}.provider"

        /**
         * Метод для создания пустого файла изображения (без самого изображения).
         * Задаем имя исходя из текущего времени и даты.
         * @return [File]
         */
        @Throws(IOException::class)
        private fun createImageFile(context: Context): File {
            // Создаем имя файла, исходя из текущих даты и времени
            val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ROOT).format(Date())
            val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            return File.createTempFile(
                "JPEG_${timeStamp}_", /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
            ).apply {
                // Сохраняем путь к файлу в глобальную переменную
                CURRENT_PHOTO_PATH = absolutePath
            }
        }

        /**
         * Метод создает пустой файл, используя [createImageFile]
         * и возвращает его Uri через [FileProvider.getUriForFile]
         *
         * @return [Uri]
         */
        fun getNewImageUri(context: Context): Uri {
            val file = createImageFile(context)
            return FileProvider.getUriForFile(
                context,
                FILE_AUTHORITY,
                file
            )
        }
    }
}








package com.molchanov.cats.utils

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContract

class GaleryContract : ActivityResultContract<String, Uri>() {
    override fun createIntent(context: Context, input: String?): Intent {
        val intent = Intent().apply {
            action = Intent.ACTION_PICK
            type = input
        }.also {
            CURRENT_PHOTO_PATH = it.data?.path.toString()
        }
        return Intent.createChooser(intent, "Выберите изображение")
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? = when {
        resultCode != RESULT_OK -> null
        else -> intent?.data
    }
}

class PhotoContract : ActivityResultContract<Uri, Uri>() {
    override fun createIntent(context: Context, input: Uri?): Intent {
        return Intent().apply{
            action = MediaStore.ACTION_IMAGE_CAPTURE
            putExtra(MediaStore.EXTRA_OUTPUT,input)
        }

//        return Intent().apply {
//            action = MediaStore.ACTION_IMAGE_CAPTURE
//        }.also { takePictureIntent ->
//            val photoFile: File? = try {
//                createImageFile()
//            } catch (ex: IOException) {
//                Log.d("M_TakeImageContract", "Ошибка при создании файла: ${ex.message}")
//                null
//            }
//            // Continue only if the File was successfully created
//            photoFile?.also {
//                val photoURI: Uri = FileProvider.getUriForFile(
//                    APP_ACTIVITY,
//                    FILE_AUTHORITY,
//                    it
//                )
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
//            }
//        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? = when {
        resultCode != RESULT_OK -> null
        else -> intent?.data
    }
}
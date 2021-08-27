package com.molchanov.cats.utils

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContract
import com.molchanov.cats.R
import com.molchanov.cats.utils.Global.CURRENT_IMAGE_URI
import com.molchanov.cats.utils.Global.CURRENT_PHOTO_PATH

class GalleryContract : ActivityResultContract<String, Boolean>() {
    override fun createIntent(context: Context, input: String?): Intent {
        val intent = Intent().apply {
            action = Intent.ACTION_PICK
            type = input
        }
        return Intent.createChooser(intent, context.getString(R.string.upload_dialog_title))
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Boolean = when {
        resultCode != RESULT_OK -> false
        resultCode == RESULT_CANCELED -> false
        else -> {
            intent?.data?.let {
                CURRENT_IMAGE_URI = it
                CURRENT_PHOTO_PATH = ""
            }
            intent != null
        }
    }
}

class PhotoContract : ActivityResultContract<Uri, Boolean>() {
    override fun createIntent(context: Context, input: Uri?): Intent {
        return Intent().apply {
            action = MediaStore.ACTION_IMAGE_CAPTURE
            putExtra(MediaStore.EXTRA_OUTPUT, input)
            input?.let { CURRENT_IMAGE_URI = it }
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Boolean = when {
        resultCode != RESULT_OK -> false
        resultCode == RESULT_CANCELED -> false
        else -> true
    }
}
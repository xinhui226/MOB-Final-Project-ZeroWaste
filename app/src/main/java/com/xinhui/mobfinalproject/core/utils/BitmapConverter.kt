package com.xinhui.mobfinalproject.core.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

object BitmapConverter {
    fun bitmapToFileToUri(bitmap: Bitmap, context: Context): Uri {
        val imagesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val imageFile = File(imagesDir, "image_${System.currentTimeMillis()}.jpg")

        val outputStream: OutputStream = FileOutputStream(imageFile)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
        outputStream.close()

        return Uri.fromFile(imageFile)
    }
}
package com.example.playlistmaker.playlist_creator.data.local

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

class FilePrivateStorage(private val context: Context) {
    fun saveImageToPrivateStorage(filePath: File, fileName: String, uri: Uri) {
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        val file = File(filePath, "${fileName}.jpg")
        val inputStream = context.contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
    }
}
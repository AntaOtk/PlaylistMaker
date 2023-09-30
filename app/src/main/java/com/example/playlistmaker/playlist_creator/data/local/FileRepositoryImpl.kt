package com.example.playlistmaker.playlist_creator.data.local

import android.net.Uri
import com.example.playlistmaker.playlist_creator.domain.FileRepository
import java.io.File

class FileRepositoryImpl(private val storage: FilePrivateStorage): FileRepository {
    override fun saveImage(filePath: File, savePlaylist: String, uri: Uri) {
        storage.saveImageToPrivateStorage(filePath,savePlaylist,uri)    }
}
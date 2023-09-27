package com.example.playlistmaker.playlist_creator.domain

import android.net.Uri
import java.io.File

interface FileRepository {
    fun saveImage(filePath: File, savePlaylist: String, uri: Uri)

}

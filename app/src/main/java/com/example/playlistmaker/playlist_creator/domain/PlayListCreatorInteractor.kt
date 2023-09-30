package com.example.playlistmaker.playlist_creator.domain

import android.net.Uri
import java.io.File

interface PlayListCreatorInteractor {
    suspend fun savePlaylist(playListName: String, description: String, fileDir: String): Long
    fun saveImage(filePath: File, savePlaylist: String, uri: Uri)
}
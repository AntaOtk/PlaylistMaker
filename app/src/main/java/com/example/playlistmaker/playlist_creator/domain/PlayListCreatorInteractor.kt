package com.example.playlistmaker.playlist_creator.domain

import android.net.Uri
import com.example.playlistmaker.library.domain.model.PlayList
import kotlinx.coroutines.flow.Flow
import java.io.File

interface PlayListCreatorInteractor {
    suspend fun createPlaylist(playListName: String, description: String, fileDir: String): Long
    fun saveImage(filePath: File, savePlaylist: String, uri: Uri)

    suspend fun updatePlayList(playList: PlayList): Flow<PlayList>
}
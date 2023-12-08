package com.example.playlistmaker.playlist_creator.domain

import android.net.Uri
import com.example.playlistmaker.library.domain.PlaylistRepository
import com.example.playlistmaker.library.domain.model.PlayList
import kotlinx.coroutines.flow.Flow
import java.io.File

class PlayListCreatorInteractorImpl(
    private val repository: PlaylistRepository,
    private val fileRepository: FileRepository
) :
    PlayListCreatorInteractor {
    override suspend fun createPlaylist(
        playListName: String,
        description: String,
        fileDir: String
    ): Long {
        val playlist = PlayList(0, playListName, description, fileDir, 0, mutableListOf())
        return repository.addPlaylist(playlist)
    }

    override fun saveImage(filePath: File, savePlaylist: String, uri: Uri) {
        fileRepository.saveImage(filePath, savePlaylist, uri)
    }

    override suspend fun updatePlayList(playList: PlayList): Flow<PlayList> {
        repository.updatePlayList(playList)
        return repository.getPlayList(playList.id)
    }

}
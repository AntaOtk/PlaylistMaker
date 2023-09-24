package com.example.playlistmaker.playlist_creator.domain

import com.example.playlistmaker.library.domain.PlaylistRepository
import com.example.playlistmaker.library.domain.model.PlayList

class PlayListCreatorInteractorImpl(private val repository: PlaylistRepository): PlayListCreatorInteractor {
    override suspend fun savePlaylist(
        playListName: String,
        description: String,
        fileDir: String
    ): Long {
        val playlist = PlayList(0,playListName, description, fileDir, 0, null)
        return repository.addPlaylist(playlist)
    }
}
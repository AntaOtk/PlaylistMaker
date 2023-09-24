package com.example.playlistmaker.playlist_creator.domain

interface PlayListCreatorInteractor {
    suspend fun savePlaylist(playListName: String, description: String, fileDir: String): Long
}
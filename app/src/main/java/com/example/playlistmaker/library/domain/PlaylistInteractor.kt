package com.example.playlistmaker.library.domain

import com.example.playlistmaker.library.domain.model.PlayList
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    fun getPlayLists(): Flow<List<PlayList>>
    suspend fun addTrack(track: Track, playList: PlayList): String
}

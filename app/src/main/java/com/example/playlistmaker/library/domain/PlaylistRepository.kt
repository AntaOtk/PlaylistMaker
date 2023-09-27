package com.example.playlistmaker.library.domain

import com.example.playlistmaker.library.domain.model.PlayList
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    fun getPlayLists(): Flow<List<PlayList>>
    suspend fun addPlaylist(playList: PlayList): Long
    suspend fun addTrack(track: Track, playList: PlayList)
    suspend fun getTrackList(playList: PlayList): List<Track>
}

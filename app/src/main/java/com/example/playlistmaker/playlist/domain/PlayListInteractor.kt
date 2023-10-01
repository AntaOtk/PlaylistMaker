package com.example.playlistmaker.playlist.domain

import com.example.playlistmaker.library.domain.model.PlayList
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface PlayListInteractor {
    fun getPlayList(playListId: Long): Flow<PlayList>
    fun getTracks(playList: PlayList):  Flow<List<Track>>
    fun getPlayListTime(tracks: List<Track>): Int
    suspend fun delete(playlist: PlayList)
}

package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface TrackHistoryRepository {
    fun getTrackList(): Flow<List<Track>>
    fun setTrack(track: Track)
    fun clear()
}

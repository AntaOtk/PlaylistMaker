package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.model.Track

interface TrackHistoryRepository {
    fun getTrackList(): List<Track>
    fun setTrack(track: Track)
    fun clear()
}
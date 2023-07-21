package com.example.playlistmaker.search.data.local

import com.example.playlistmaker.search.data.dto.TrackDto

interface LocalStorage {
    fun getTrackList(): List<TrackDto>
    fun clear()
    fun setTrack(track: TrackDto)
}
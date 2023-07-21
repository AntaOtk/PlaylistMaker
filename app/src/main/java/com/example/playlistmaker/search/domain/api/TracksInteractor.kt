package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.model.Track

interface TracksInteractor {
    fun searchTracks(expression: String, consumer: TracksConsumer)
    fun getEmptyMessage(): String

    fun interface TracksConsumer {
        fun consume(tracks: List<Track>?, errorMessage: String?)
    }
    fun getTrackList(): List<Track>
    fun setTrack(track: Track)
    fun clear()
}
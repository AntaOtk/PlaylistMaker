package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.models.Track

interface TracksInteractor {
    fun searchTracks(expression: String, consumer: TracksConsumer, errorHandler: ErrorHandler)

    fun interface TracksConsumer {
        fun consume(tracks: List<Track>)
    }

     fun interface ErrorHandler {
         fun handle()

     }
}
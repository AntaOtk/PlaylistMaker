package com.example.playlistmaker.domain.api

import android.database.DatabaseErrorHandler
import com.example.playlistmaker.domain.models.Track

interface TracksInteractor {
    fun searchTracks(expression: String, consumer: TracksConsumer,errorHandler: ErrorHandler)

    fun interface TracksConsumer {
        fun consume(tracks: List<Track>)
    }

     fun interface ErrorHandler {
         fun handle()

     }
}
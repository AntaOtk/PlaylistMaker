package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.data.NetworkException
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.models.Track
import java.util.concurrent.Executors

class TracksInteractorImpl(private val repository: TracksRepository) : TracksInteractor {
    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(
        expression: String,
        consumer: TracksInteractor.TracksConsumer,
        errorHandler: TracksInteractor.ErrorHandler
    ) {
        executor.execute {
            val result = try {
                 repository.searchTracks(expression)
            } catch (e: NetworkException) {
                errorHandler.handle()
                return@execute
            }
            consumer.consume(result)
        }
    }
}

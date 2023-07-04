package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.data.NetworkException
import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.api.TracksRepository
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

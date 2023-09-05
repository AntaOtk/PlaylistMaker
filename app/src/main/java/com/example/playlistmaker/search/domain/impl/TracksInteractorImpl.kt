package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.api.TrackHistoryRepository
import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TracksInteractorImpl(
    private val searchRepository: TracksRepository,
    private val historyRepository: TrackHistoryRepository
) : TracksInteractor {
    override fun searchTracks(expression: String): Flow<Pair<List<Track>?, String?>> {
        return searchRepository.searchTracks(expression).map { result ->
            when (result) {
                is Resource.Success -> {
                    Pair(result.data, null)
                }

                is Resource.Error -> {
                    Pair(null, result.message)
                }
            }
        }
    }

    override fun getEmptyMessage(): String {
        return searchRepository.getMessage()
    }

    override fun getTrackList(): Flow<List<Track>> {
        return historyRepository.getTrackList()
    }

    override fun setTrack(track: Track) {
        historyRepository.setTrack(track)
    }

    override fun clear() {
        historyRepository.clear()
    }
}

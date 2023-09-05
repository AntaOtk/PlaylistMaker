package com.example.playlistmaker.search.data

import com.example.playlistmaker.library.data.db.AppDatabase
import com.example.playlistmaker.search.data.local.LocalStorage
import com.example.playlistmaker.search.data.mapper.TrackConvertor
import com.example.playlistmaker.search.domain.api.TrackHistoryRepository
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class TrackHistoryRepositoryImpl(
    private val localStorage: LocalStorage,
    private val appDatabase: AppDatabase,
    private val mapper: TrackConvertor
) : TrackHistoryRepository {

    override fun getTrackList(): Flow<List<Track>> = flow {
        emit(localStorage.getTrackList().map {
            val favoriteTracks = appDatabase.trackDao().getTrackId()
            mapper.trackMap(it, favoriteTracks.contains(it.trackId))
        })
    }

    override fun setTrack(track: Track) {
        val trackDto = mapper.trackDtoMap(track)
        localStorage.setTrack(trackDto)
    }

    override fun clear() {
        localStorage.clear()
    }
}

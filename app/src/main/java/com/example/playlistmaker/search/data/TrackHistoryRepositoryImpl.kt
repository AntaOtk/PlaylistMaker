package com.example.playlistmaker.search.data

import com.example.playlistmaker.search.data.local.LocalStorage
import com.example.playlistmaker.search.data.mapper.TrackMapper
import com.example.playlistmaker.search.domain.api.TrackHistoryRepository
import com.example.playlistmaker.search.domain.model.Track


class TrackHistoryRepositoryImpl(
    private val localStorage: LocalStorage,
    private val mapper: TrackMapper
) : TrackHistoryRepository {

    override fun getTrackList(): List<Track> {
        return localStorage.getTrackList().map {
            mapper.trackMap(it)
        }
    }

    override fun getTrack(): Track {
        return mapper.trackMap(localStorage.getLastTrack())
    }

    override fun setTrack(track: Track) {
        val trackDto = mapper.trackDtoMap(track)
        localStorage.setTrack(trackDto)
    }

    override fun clear() {
        localStorage.clear()
    }
}

package com.example.playlistmaker.search.data

import com.example.playlistmaker.search.data.local.LocalStorage
import com.example.playlistmaker.search.domain.api.TrackHistoryRepository
import com.example.playlistmaker.search.domain.mapper.TrackMapper
import com.example.playlistmaker.search.domain.model.Track


class SearchHistoryRepository(
    private val localStorage: LocalStorage,
) : TrackHistoryRepository {

    private val mapper = TrackMapper()

    override fun getTrackList(): List<Track> {
        return localStorage.getTrackList().map {
            mapper.trackMap(it)
        }
    }

    override fun setTrack(track: Track) {
        val trackDto = mapper.trackDtoMap(track)
        localStorage.setTrack(trackDto)
    }


    override fun clear() {
        localStorage.clear()
    }

}

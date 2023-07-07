package com.example.playlistmaker.search.data

import com.example.playlistmaker.search.data.dto.TrackDto
import com.example.playlistmaker.search.domain.api.OneTrackRepository
import com.example.playlistmaker.search.domain.api.TrackHistoryRepository
import com.example.playlistmaker.search.domain.model.Track


class SearchHistoryRepository(
    private val localStorage: LocalStorage,
) : OneTrackRepository, TrackHistoryRepository {

    private val mapper = TrackMapper()

    override fun getTrack(): Track {

        return mapper.trackMap(localStorage.getTrack())
    }

    override fun getTrackList(): List<Track> {
        return localStorage.getTrackList().map {
            mapper.trackMap(it)
        }
    }

    override fun setTrack(track: Track) {
        val trackDto = TrackDto(
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl
        )
        localStorage.setTrack(trackDto)
    }


    override fun clear() {
        localStorage.clear()
    }

}

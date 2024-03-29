package com.example.playlistmaker.library.data

import com.example.playlistmaker.library.data.db.entity.TracksEntity
import com.example.playlistmaker.search.domain.model.Track


class TrackDbMapper {
    fun map(track: Track, updateTime: String): TracksEntity {
        return TracksEntity(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl,
            updateTime
        )
    }

    fun map(track: TracksEntity): Track {
        return Track(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl,
        )
    }
}

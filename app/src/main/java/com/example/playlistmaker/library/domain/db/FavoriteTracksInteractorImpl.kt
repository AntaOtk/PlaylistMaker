package com.example.playlistmaker.library.domain.db

import com.example.playlistmaker.library.domain.FavoriteTracksInteractor
import com.example.playlistmaker.library.domain.FavoriteTracksRepository
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

class FavoriteTracksInteractorImpl(private val favoriteTracksRepository: FavoriteTracksRepository) :
    FavoriteTracksInteractor {
    override fun showTracks(): Flow<List<Track>> {
        return favoriteTracksRepository.getFavoriteTracks()
    }

    override suspend fun updateFavorite(track: Track): Track {
        return if (track.favoriteChecked) {
            favoriteTracksRepository.deleteFavoriteTrack(track)
            track.favoriteChecked = false
            track
        } else {
            track.favoriteChecked = true
            favoriteTracksRepository.addFavoriteTrack(track)
            track
        }
    }
}

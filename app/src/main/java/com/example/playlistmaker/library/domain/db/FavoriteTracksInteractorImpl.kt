package com.example.playlistmaker.library.domain.db

import com.example.playlistmaker.library.domain.FavoriteTracksInteractor
import com.example.playlistmaker.library.domain.FavoriteTracksRepository
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

class FavoriteTracksInteractorImpl(private val favoriteTracksRepository: FavoriteTracksRepository) :
    FavoriteTracksInteractor {

    private var isChacked = false
    override fun getTracks(): Flow<List<Track>> {
        return favoriteTracksRepository.getFavoriteTracks()
    }

    override suspend fun updateFavorite(track: Track): Boolean {
        favoriteTracksRepository.getFavoriteChecked().collect{tracksId ->
            isChacked = if (tracksId.contains(track.trackId)) {
                favoriteTracksRepository.deleteFavoriteTrack(track)
                false
            } else {
                favoriteTracksRepository.addFavoriteTrack(track)
                true
            }
        }
        return isChacked
    }

    override suspend fun getChecked(tracksId: Long) : Boolean {
        favoriteTracksRepository.getFavoriteChecked().collect { id ->
            isChacked = id.contains(tracksId)
        }
        return isChacked
    }

}

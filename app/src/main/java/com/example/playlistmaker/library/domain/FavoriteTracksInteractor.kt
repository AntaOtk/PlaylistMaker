package com.example.playlistmaker.library.domain

import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTracksInteractor {
    fun showTracks(): Flow<List<Track>>
    suspend fun updateFavorite(track: Track): Track
}

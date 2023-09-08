package com.example.playlistmaker.library.domain

import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTracksInteractor {
    fun getTracks(): Flow<List<Track>>
    suspend fun updateFavorite(track: Track): Boolean
    suspend fun getChecked(tracksId: Long): Boolean
}

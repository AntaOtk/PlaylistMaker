package com.example.playlistmaker.library.domain

import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTracksRepository {
    fun getFavoriteTracks(): Flow<List<Track>>
    suspend fun  addFavoriteTrack(track: Track)
    suspend fun deleteFavoriteTrack(track: Track)
    fun getFavoriteChecked(): Flow<List<Long>>
}

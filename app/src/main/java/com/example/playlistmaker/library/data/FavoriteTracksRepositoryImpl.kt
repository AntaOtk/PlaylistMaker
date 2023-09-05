package com.example.playlistmaker.library.data

import com.example.playlistmaker.library.data.db.AppDatabase
import com.example.playlistmaker.library.data.db.entity.TracksEntity
import com.example.playlistmaker.library.domain.FavoriteTracksRepository
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.util.Date


class FavoriteTracksRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: TrackDbConvertor,
) : FavoriteTracksRepository {
    override fun getFavoriteTracks(): Flow<List<Track>> = flow {
        val tracks = appDatabase.trackDao().getFavoriteTracks()
        emit(convertFromTrackEntity(tracks))
    }

    override fun getFavoriteChecked(id: Long): Flow<List<Long>> = flow {
        val tracksId = appDatabase.trackDao().getTrackId()
        emit(tracksId)
    }

    override suspend fun addFavoriteTrack(track: Track) {
        val trackEntity = convertToTrackEntity(track)
        appDatabase.trackDao().insertTrack(trackEntity)
    }

    override suspend fun deleteFavoriteTrack(track: Track) {
        val trackEntity = convertToTrackEntity(track)
        appDatabase.trackDao().delete(trackEntity)
    }

    private fun convertFromTrackEntity(tracks: List<TracksEntity>): List<Track> {
        return tracks.map { track -> trackDbConvertor.map(track) }
    }

    private fun convertToTrackEntity(track: Track): TracksEntity {
        return trackDbConvertor.map(track, SimpleDateFormat("yyyy MM dd HH:mm:ss").format(Date()))
    }
}

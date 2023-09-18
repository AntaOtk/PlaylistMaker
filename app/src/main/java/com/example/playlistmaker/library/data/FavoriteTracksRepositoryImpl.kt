package com.example.playlistmaker.library.data

import android.annotation.SuppressLint
import com.example.playlistmaker.library.data.db.TrackDao
import com.example.playlistmaker.library.data.db.entity.TracksEntity
import com.example.playlistmaker.library.domain.FavoriteTracksRepository
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.util.Date


class FavoriteTracksRepositoryImpl(
    private val dao: TrackDao,
    private val trackDbMapper: TrackDbMapper,
) : FavoriteTracksRepository {


    override fun getFavoriteTracks(): Flow<List<Track>> = flow {
        val tracks = dao.getFavoriteTracks()
        emit(convertFromTrackEntity(tracks))
    }

    override fun getFavoriteChecked(): Flow<List<Long>> = flow {
        val tracksId = dao.getTracksId()
        emit(tracksId)
    }

    override suspend fun addFavoriteTrack(track: Track) {
        val trackEntity = convertToTrackEntity(track)
        dao.insertTrack(trackEntity)
    }

    override suspend fun deleteFavoriteTrack(track: Track) {
        val trackEntity = convertToTrackEntity(track)
        dao.delete(trackEntity)
    }

    private fun convertFromTrackEntity(tracks: List<TracksEntity>): List<Track> {
        return tracks.map { track -> trackDbMapper.map(track) }
    }

    @SuppressLint("SimpleDateFormat")
    private fun convertToTrackEntity(track: Track): TracksEntity {
        return trackDbMapper.map(track, SimpleDateFormat("yyyy MM dd HH:mm:ss").format(Date()))
    }
}

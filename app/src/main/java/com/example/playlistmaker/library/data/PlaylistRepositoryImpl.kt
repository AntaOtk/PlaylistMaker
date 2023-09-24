package com.example.playlistmaker.library.data

import com.example.playlistmaker.library.data.db.PlayListDao
import com.example.playlistmaker.library.data.db.entity.PlayListEntity
import com.example.playlistmaker.library.domain.PlaylistRepository
import com.example.playlistmaker.library.domain.model.PlayList
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistRepositoryImpl(
    private val dao: PlayListDao,
    private val jsonMapper: JsonMapper,
    private val playListDbMapper: PlayListDbMapper
) : PlaylistRepository {

    override fun getPlayLists(): Flow<List<PlayList>> = flow {
        val playLists = dao.getPlayLists()
        emit(convertFromEntity(playLists))
    }

    private fun convertFromEntity(playLists: List<PlayListEntity>): List<PlayList> {
        return playLists.map { playList -> playListDbMapper.map(playList) }
    }

    override suspend fun addPlaylist(playList: PlayList): Long {
        val playListEntity = convertToTrackEntity(playList)
        return dao.insertPlaylist(playListEntity)
    }

    override suspend fun addTrack(track: Track, playList: PlayList) {
        ++playList.trackCount
        playList.tracks = jsonMapper.setTrack(track, playList.tracks)
        val playListEntity = convertToTrackEntity(playList)
        dao.updatePlayList(playListEntity)
    }

    override suspend fun getTrackList(playList: PlayList): List<Track> {
        val tracks = dao.getTrackList(playList.id).first()
        return jsonMapper.convertToList(tracks)

    }


    private fun convertToTrackEntity(playList: PlayList): PlayListEntity {
        return playListDbMapper.map(playList)
    }
}
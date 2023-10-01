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

    override fun getPlayList(playListId: Long): Flow<PlayList> = flow {
        val playList = dao.getPlayList(playListId).first()
        emit(convertPayListFromEntity(playList))
    }

    override suspend fun addPlaylist(playList: PlayList): Long {
        val playListEntity = convertToTrackEntity(playList)
        return dao.insertPlaylist(playListEntity)
    }

    override suspend fun addTrack(track: Track, playList: PlayList) {
        ++playList.trackCount
        playList.tracks.add(track)
        val playListEntity = convertToTrackEntity(playList)
        dao.updatePlayList(playListEntity)
    }

    override suspend fun getTrackList(playListId: Long): List<Track> {
        val tracks = dao.getTrackList(playListId).first()
        return jsonMapper.convertToList(tracks)

    }

    override suspend fun delete(playlist: PlayList) {
        val playListEntity = convertToTrackEntity(playlist)
        dao.delete(playListEntity)
    }


    private fun convertToTrackEntity(playList: PlayList): PlayListEntity {
        val tracks = jsonMapper.convertFromList(playList.tracks)
        return playListDbMapper.map(playList, tracks)
    }
    private fun convertPayListFromEntity(playList: PlayListEntity): PlayList {
        val tracks = jsonMapper.convertToList(playList.tracks)
        return playListDbMapper.map(playList,tracks)
    }
    private fun convertFromEntity(playLists: List<PlayListEntity>): List<PlayList> {
        return playLists.map { playList ->
            val tracks = jsonMapper.convertToList(playList.tracks)
            playListDbMapper.map(playList, tracks)
        }
    }
}
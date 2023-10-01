package com.example.playlistmaker.playlist.domain

import com.example.playlistmaker.library.domain.PlaylistRepository
import com.example.playlistmaker.library.domain.model.PlayList
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.Duration

class PlayListInteractorImpl(private val repository: PlaylistRepository) : PlayListInteractor {
    override fun getPlayList(playListId: Long): Flow<PlayList> {
        return repository.getPlayList(playListId)
    }

    override fun getTracks(playList: PlayList): Flow<List<Track>> = flow {
        emit(repository.getTrackList(playList.id))
    }

    override fun getPlayListTime(tracks: List<Track>): Int {
        var playListTime = 0L
        for (track in tracks) {
            playListTime += track.trackTimeMillis
        }
        val duration = Duration.ofMillis(playListTime)
        val minutes = duration.toMinutes()
        return (minutes).toInt()
    }

    override suspend fun delete(playlist: PlayList) {
        repository.delete(playlist)
    }


}
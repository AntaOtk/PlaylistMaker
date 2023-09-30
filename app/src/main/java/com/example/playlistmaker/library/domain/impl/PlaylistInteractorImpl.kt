package com.example.playlistmaker.library.domain.impl

import com.example.playlistmaker.library.domain.PlaylistInteractor
import com.example.playlistmaker.library.domain.PlaylistRepository
import com.example.playlistmaker.library.domain.model.PlayList
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(private val playlistRepository: PlaylistRepository) :
    PlaylistInteractor {
    override fun getPlayLists(): Flow<List<PlayList>> {
        return playlistRepository.getPlayLists()
    }

    override suspend fun addTrack(track: Track, playList: PlayList): Boolean {
        val tracks = playlistRepository.getTrackList(playList)
        return if (tracks.isEmpty() || !tracks.contains(track)) {
            playlistRepository.addTrack(track, playList)
            true
        } else false

    }
}

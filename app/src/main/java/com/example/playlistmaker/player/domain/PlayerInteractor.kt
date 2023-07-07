package com.example.playlistmaker.player.domain

import com.example.playlistmaker.search.domain.model.Track

interface PlayerInteractor {

    fun preparePlayer(track: Track, prepareCallback: () -> Unit, completeCallback: () -> Unit)
    fun startPlayer()
    fun pausePlayer()
    fun getCurrentPosition(): Int
    fun release()
}
